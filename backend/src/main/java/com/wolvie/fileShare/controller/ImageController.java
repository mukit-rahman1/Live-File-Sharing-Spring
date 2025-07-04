package com.wolvie.fileShare.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Set;

import com.wolvie.fileShare.service.S3Service;
import com.wolvie.fileShare.config.TransferTracker;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final S3Service s3Service;
    private final SimpMessagingTemplate messagingTemplate;
    private final TransferTracker transferTracker;

    public ImageController(
            S3Service s3Service,
            SimpMessagingTemplate messagingTemplate,
            TransferTracker transferTracker) {
        this.s3Service = s3Service;
        this.messagingTemplate = messagingTemplate;
        this.transferTracker = transferTracker;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile file,
            Principal principal) {
        try {
            String username = principal.getName(); 
            String imageUrl = s3Service.uploadFile(file, username);
            return ResponseEntity.ok().body("Image uploaded successfully: " + imageUrl);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/view")
    public ResponseEntity<?> getUserImages(Principal principal) {
        String username = principal.getName();
        List<String> userImagesUrls = s3Service.getUserImages(username);
        Map<String, List<String>> response = new HashMap<>();
        response.put("Images", userImagesUrls);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-image")
    public ResponseEntity<?> sendImage(
            @RequestParam String recipient,
            @RequestParam String imageUrl) {
        messagingTemplate.convertAndSendToUser(recipient, "/queue/images", imageUrl);
        return ResponseEntity.ok("Image sent successfully");
    }

    @GetMapping("/transfer-peers")
    public ResponseEntity<?> getOnlinePeers(Principal principal) {
        String username = principal.getName();
        Set<String> peers = transferTracker.getOnline();
        peers = peers.stream().filter(u -> !u.equals(username)).collect(Collectors.toSet());
        return ResponseEntity.ok(peers);
    }
}
