import { useNavigate } from "react-router-dom";

function Landing() {
const navigate = useNavigate();

const handleNavReg = () => {
    navigate('/register')
}

const handleNavLogin = () => {
    navigate('/login')
}

    return (
        <div className="landing">
            <div className="register-box" onClick={handleNavReg}>
                <h3>Register</h3>
            </div>
            <a className="login-box" onClick={handleNavLogin}>
                <h3>Login</h3>
            </a>
        </div>
    )
}

export default Landing;
