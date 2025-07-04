import axios from "axios";
import TopBar from "./TopBar";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";

function LoginPage() {
    const navigate = useNavigate();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = async () => {
        try {
            const response = await axios.post('http://localhost:8080/api/v1/auth/login', {
                username,
                password
            },
            {
                withCredentials: true
            }
        );

            console.log("User logged in and cookie set", response);

            navigate('/homepage');

        } catch (error: any) {
            console.log(error.response ? error.response : error.data);
        }
    }

    return (<>
        <head>
            <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined:opsz,wght,FILL,GRAD@48,700,0,0" />

        </head>
        <TopBar></TopBar>

        <div className="login-container">
            <div className="back">
                <Link to="/">
                    <svg xmlns="http://www.w3.org/2000/svg" height="48px" viewBox="0 -960 960 960" width="48px" fill="#000000"><path d="m297.18-440.39 238.95 238.96L480-145.87 145.87-480 480-814.7l56.13 56.13-238.95 238.96H814.7v79.22H297.18Z" /></svg>
                </Link>
            </div>
            <input placeholder="enter username" className="username" value={username} onChange={(e) => setUsername(e.target.value)}></input>
            <input placeholder="enter password" type="password" className="password" value={password} onChange={(e) => setPassword(e.target.value)}></input>
            <div className="reg-submit" onClick={handleLogin}>
                <h3>Submit</h3>
            </div>
        </div>

    </>
    )
}

export default LoginPage;