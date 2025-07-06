import axios from "axios";
import { useEffect, useState } from "react";
import TopBar from "./TopBar";
import { Link } from 'react-router-dom';

function ViewImages() {
    const [images, setImages] = useState<string[]>([]);

    useEffect(() => {
        const fetchImages = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await axios.get('http://localhost:8080/api/v1/images/view', {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                console.log('api response:', response.data);
                setImages(response.data.Images);
            } catch (error: any) {
                console.log('error:', error.response ? error.response : error.message);
            }
        };
        fetchImages();
    }, []);

    return (
        <>
            <TopBar />

            <div className='background'>
                <div className="back">
                    <Link to="/homepage">
                        <svg xmlns="http://www.w3.org/2000/svg" height="48px" viewBox="0 -960 960 960" width="48px" fill="#000000"><path d="m297.18-440.39 238.95 238.96L480-145.87 145.87-480 480-814.7l56.13 56.13-238.95 238.96H814.7v79.22H297.18Z" /></svg>
                    </Link>
                </div>

                <ul className="image-grid">
                    {images.map((image, index) => (
                        <li key={index} className="image-item">
                            <img
                                src={image}
                                alt={`User Image ${index}`}
                                style={{ width: "150px", height: "auto", maxWidth: "100%", borderRadius: "6px" }}
                            />
                        </li>
                    ))}
                </ul>
            </div>
        </>
    );
}

export default ViewImages;
