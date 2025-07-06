import TopBar from "./TopBar";
import { useState } from "react";
import axios from "axios";

function RegisterPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleCreate = async () => {
        try {
          const response = await axios.post(
            'http://localhost:8080/api/v1/auth/register',
            { username, password }
          );
      
          console.log("User created:", response.data);
      
          const token = response.data.token;
          localStorage.setItem('token', token);
      
          window.location.href = '/homepage';
      
        } catch (error: any) {
          const msg = error?.response?.data?.message || error.message || 'Registration failed';
          setError(msg);
          console.log(error);
        }
      };

    return (
        <>
            <TopBar />

            <div className="register-container">
                <div className="back">
                    <a href="/">
                        <svg xmlns="http://www.w3.org/2000/svg" height="48px" viewBox="0 -960 960 960" width="48px" fill="#000000">
                            <path d="m297.18-440.39 238.95 238.96L480-145.87 145.87-480 480-814.7l56.13 56.13-238.95 238.96H814.7v79.22H297.18Z" />
                        </svg>
                    </a>
                </div>

                <form onSubmit={(e) => {
                    e.preventDefault();
                    handleCreate();
                }}>
                    <input
                        type="text"
                        placeholder="Enter username"
                        className="name"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                    <input
                        type="password"
                        placeholder="Enter password"
                        className="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                    {error && <div className="error">{error}</div>}
                    <button type="submit" className="reg-submit">
                        <h3>Submit</h3>
                    </button>
                </form>
            </div>
        </>
    );
}

export default RegisterPage;
