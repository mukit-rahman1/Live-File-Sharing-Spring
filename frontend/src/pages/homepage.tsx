import axios from "axios";
import TopBar from "./TopBar";
import { useNavigate } from "react-router-dom";

function HomePage() {

    const navigate = useNavigate();

    const handleHomeConnect = () => {
        navigate('/imagetransfer');
    };

    const handleUploadPage = () => {
        navigate('/uploadimage');
    };

    const handleViewPage = () => {
        navigate('/viewstoredimages');
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/');
    };

    const token = localStorage.getItem('token');

    axios.get('/api/v1/auth/login', {
        headers: {
            Authorization: `Bearer ${token}`
        }
    })
    .then(response => {
        console.log(response.data);       
    }).catch(error => {
        console.log('Error:', error);
    })

    return (<>

        <TopBar></TopBar>
        <div className="homepage-container">
            <div className="logout-button" onClick={handleLogout}>
                <h3>Logout</h3>
            </div>

            <div className="button-group">
                <div className="upload-button" onClick={handleUploadPage}>
                    <h3>Upload images</h3>
                </div>
                <div className="view-button" onClick={handleViewPage}>
                    <h3>View images</h3>
                </div>
                <div className="conn-button" onClick={handleHomeConnect}>
                    <h3>Connect and Receive</h3>
                </div>
            </div>
        </div>

    </>)
}

export default HomePage;