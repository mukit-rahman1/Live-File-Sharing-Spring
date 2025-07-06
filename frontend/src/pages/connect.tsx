import { useEffect, useState } from "react";
import TopBar from "./TopBar";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";
import axios from "axios";

function ImageTransferPage() {
  const [peers, setPeers] = useState<string[]>([]);
  const [myImages, setMyImages] = useState<string[]>([]);
  const [receivedImages, setReceivedImages] = useState<string[]>([]);
  const [selectedPeer, setSelectedPeer] = useState<string | null>(null);
  const [selectedImage, setSelectedImage] = useState<string | null>(null);

  useEffect(() => {
    // STOMP Client with cookie-based session (no connectHeaders)
    const token = localStorage.getItem('token');
    const client = new Client({
      webSocketFactory: () => new SockJS(`http://localhost:8080/ws?token=${token}`),
      connectHeaders: {
        Authorization: `Bearer ${token}`,
      }
    });

    client.onConnect = () => {
      console.log("Connected to WebSocket");

      // Register self as active transfer window
      client.publish({
        destination: "/app/register-transfer-window",
      });

      // Listen for incoming images
      client.subscribe("/user/queue/images", (message) => {
        const newImage = message.body;
        console.log("Received:", newImage);
        setReceivedImages((prev) => [newImage, ...prev]);
      });

      // Fetch online peers
      axios
        .get("http://localhost:8080/api/v1/images/transfer-peers", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((res) => {
          console.log("Peers:", res.data);
          setPeers(res.data);
        })
        .catch(console.error);

      // Fetch own saved images
      axios
        .get("http://localhost:8080/api/v1/images/view", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((res) => {
          console.log("My images:", res.data.Images);
          setMyImages(res.data.Images);
        })
        .catch(console.error);
    };

    client.activate();
    return () => {
      client.deactivate();
    };
  }, []);

  const handleSendImage = async () => {
    if (!selectedPeer || !selectedImage) {
      alert("Select both peer and image!");
      return;
    }

    try {
      const token = localStorage.getItem('token'); 
      await axios.post(
        "http://localhost:8080/api/v1/images/send-image",
        null,
        {
          params: {
            recipient: selectedPeer,
            imageUrl: selectedImage,
          },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      alert(`Image sent to ${selectedPeer}!`);
    } catch (err) {
      console.error(err);
      alert("Failed to send.");
    }
  };

  return (
    <>
      <TopBar />
      <div style={{ padding: "20px", backgroundColor: "white"}}>
        <h2>Image Transfer</h2>

        <h3>Nearby Devices:</h3>
        <ul>
          {peers.length === 0 && <li>No other devices online</li>}
          {peers.map((p) => (
            <li key={p}>
              {p}{" "}
              <button onClick={() => setSelectedPeer(p)}>
                {selectedPeer === p ? "Selected" : "Select"}
              </button>
            </li>
          ))}
        </ul>

        <h3>Your Saved Images:</h3>
        <ul style={{ display: "flex", flexWrap: "wrap", gap: "10px" }}>
          {myImages.length === 0 && <li>No saved images</li>}
          {myImages.map((img, i) => (
            <li key={i}>
              <img src={img} alt={`saved-${i}`} width="150" />
              <br />
              <button onClick={() => setSelectedImage(img)}>
                {selectedImage === img ? "Chosen" : "Choose"}
              </button>
            </li>
          ))}
        </ul>

        {selectedPeer && selectedImage && (
          <button onClick={handleSendImage} style={{ marginTop: "20px" }}>
            Send to {selectedPeer}
          </button>
        )}

        <h3 style={{ color: "blue" }}>Received Images:</h3>
        <ul style={{ display: "flex", flexWrap: "wrap", gap: "10px" }}>
          {receivedImages.map((img, i) => (
            <li key={i}>
              <img src={img} alt={`received-${i}`} width="150" />
            </li>
          ))}
        </ul>
      </div>
    </>
  );
}

export default ImageTransferPage;
