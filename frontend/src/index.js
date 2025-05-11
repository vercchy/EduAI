// TODO: Dodaj slikicki mali za footerot (telefon,email,etc)

import React from 'react';
import ReactDOM from 'react-dom/client';
import {Routes, Route, BrowserRouter} from 'react-router-dom';
import reportWebVitals from './reportWebVitals';

import Login from './pages/Login';
import Student from "./pages/student";


import 'bootstrap/dist/css/bootstrap.min.css';
import './index.css';
import logo from './assets/eduAI_logo.svg';
import zhurka_slika from './assets/zhurkaaa.svg';

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals

reportWebVitals();



function App() {
    return (
        <>
            {/* Navbar */}
            <nav className="navbar navbar-expand-lg navbar-light bg-white shadow-sm px-4">
                <a className="navbar-brand" href="#">
                    <img src={logo} alt="EduAI Logo" width="80" height="80"/>
                </a>
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarContent">
                    <ul className="navbar-nav ms-4">
                        <li className="nav-item"><a className="nav-link" href="#">Home</a></li>
                        <li className="nav-item"><a className="nav-link" href="#">Courses</a></li>
                        <li className="nav-item"><a className="nav-link" href="#">About Us</a></li>
                        <li className="nav-item"><a className="nav-link" href="#">Contact</a></li>
                    </ul>
                    <div className="ms-auto">
                        <a className="btn btn-link" href="#">Sign Up</a>
                        <a className="btn btn-custom-color text-white ms-2" href="#">Login</a>
                    </div>
                </div>
            </nav>

            {/* Sign Up Section */}
            <div className="container my-5">
                <div className="row align-items-center">
                    {/* Form */}
                    <div className="col-md-6">
                        <h2 className="fw-bold mb-2">Sign Up</h2>
                        <p className="mb-4 text-muted">Create an account for free.</p>

                        <form>
                            <div className="mb-3">
                                <label className="form-label">Full Name</label>
                                <input type="text" className="form-control" placeholder="Enter your Name" />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Email</label>
                                <input type="email" className="form-control" placeholder="Enter your Email" />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Password</label>
                                <input type="password" className="form-control" placeholder="Enter your Password" />
                            </div>

                            <div className="form-check mb-3">
                                <input className="form-check-input" type="checkbox" />
                                <label className="form-check-label">
                                    I agree with <a href="#">Terms of Use</a> and <a href="#">Privacy Policy</a>
                                </label>
                            </div>

                            <button type="submit" className="btn btn-custom-color w-100 text-white mb-3">Sign Up</button>
                            <div className="text-center mb-3 text-muted">OR</div>
                            <button type="button" className="btn btn-light w-100 border">
                                <img src="https://img.icons8.com/color/16/000000/google-logo.png" alt="google" className="me-2" />
                                Sign Up with Google
                            </button>

                            <p className="text-center mt-4">
                                Already have an account? <a href="#">Login</a> <span>↗</span>
                            </p>
                        </form>
                    </div>

                    {/* Illustration */}
                    <div className="col-md-6 text-center">
                        <img src={zhurka_slika} alt="Zhurka" width="500" height="500" />
                    </div>
                </div>
            </div>

            {/* Footer */}
            <footer className="bg-light py-5 mt-5">
                <div className="container">
                    <div className="row text-center text-md-start">
                        <div className="col-md-3 mb-4">
                            <h5>EduAI</h5>
                            <p><i className="bi bi-envelope"></i> hello@eduai.com</p>
                            <p><i className="bi bi-telephone"></i> +389 70 123 456</p>
                            <p><i className="bi bi-geo-alt"></i> Skopje</p>
                        </div>

                        <div className="col-md-3 mb-4">
                            <h6>Home</h6>
                            <ul className="list-unstyled">
                                <li>Benefits</li>
                                <li>Our Courses</li>
                                <li>Our FAQ</li>
                            </ul>
                        </div>

                        <div className="col-md-3 mb-4">
                            <h6>About Us</h6>
                            <ul className="list-unstyled">
                                <li>Company</li>
                                <li>Achievements</li>
                                <li>Our Goals</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </footer>
        </>
    );
}

// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//     <React.StrictMode>
//         <App />
//     </React.StrictMode>
// );
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/home" element={<App />} />
                <Route path="/login" element={<Login/>} />
                <Route path="/student" element={<Student/>} />
                {/* Add other routes here, e.g. Home */}
            </Routes>
        </BrowserRouter>
    </React.StrictMode>
);
export default App;


