import axios from 'axios';
import TopBar from './TopBar';
import { useState } from 'react';
import { Link } from 'react-router-dom';

function UploadPage () {
    const [status, setStatus] = useState('');
    const [file, setFile] = useState(null);

    const handleFileChange = (event: any) => {
        setFile(event.target.files[0]);
    }
    const handleUpload = async () => {
        if (!file) {
          setStatus('No file selected');
          return;
        }
      
        const formData = new FormData();
        formData.append('file', file);
      
        try {
          const token = localStorage.getItem('token');
          console.log(token);
          await axios.post('http://localhost:8080/api/v1/images/upload', formData, {
            headers: {
              'Content-Type': 'multipart/form-data',
              'Authorization': `Bearer ${token}`
            }
          });
          setStatus('Uploaded');
        } catch (error: any) {
          setStatus('Upload failed');
          console.log(error.response ? error.response : error.data);
        }
      };


    return( <>
    <head>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,700,0,0" />
    </head>


    <TopBar></TopBar>

    <div className='background'>
    <div className="back">
                <Link to="/homepage">
                    <svg xmlns="http://www.w3.org/2000/svg" height="48px" viewBox="0 -960 960 960" width="48px" fill="#000000"><path d="m297.18-440.39 238.95 238.96L480-145.87 145.87-480 480-814.7l56.13 56.13-238.95 238.96H814.7v79.22H297.18Z" /></svg>
                </Link>
            </div>
        <input type='file' onChange={handleFileChange}/>
        <button onClick={handleUpload}>Upload Image</button>
        <h3>{status}</h3>
    </div>

    </>)
}

export default UploadPage;