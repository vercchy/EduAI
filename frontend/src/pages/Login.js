// TODO: Dodaj slikicki mali za footerot (telefon,email,etc)

import React, { useState } from 'react';
import axios from 'axios';
import ReactDOM from 'react-dom/client';
import reportWebVitals from '../reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../index.css';
import logo from '../assets/eduAI_logo.svg';
import zhurka_slika from '../assets/zhurkaaa.svg';
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import { root } from "../index"
import {useNavigate} from "react-router-dom";


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

function Login() {
    const navigate = useNavigate(); // Hook to navigate after successful login
    const [formData, setFormData] = useState({
        email: '',
        password: ''
    });

    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        try {
            const response = await axios.post('http://localhost:9090/auth/login', {
                email: formData.email,
                password: formData.password
            });

            // Assuming the response contains the token
            localStorage.setItem('token', response.data.token); // Store token in localStorage

            setSuccess('Login successful!'); // You can use this to show a success message

            // Redirect the user to the dashboard or another protected route
            navigate('/student');
        } catch (err) {
            setError(err.response?.data || 'Login failed');
        }
    };
    return (
        <>
            <Navbar/>

            {/* Sign Up Section */}
            <div className="container my-5">
                <div className="row align-items-center">

                    {/* Testimonial (Left) */}
                    <div className="col-md-6 mb-4 mb-md-0 pe-md-5">
                        <h2 className="fw-bold mb-3">Students Testimonials</h2>
                        <p className="text-muted mb-4">
                            Lorem ipsum dolor sit amet consectetur. Tempus tincidunt etiam eget elit id imperdiet et.
                            Cras eu sit dignissim lorem nibh et. Ac cum eget habitasse in velit fringilla feugiat senectus in.
                        </p>

                        <div className="bg-white p-4 rounded-4 shadow-sm border">
                            <p className="text-secondary mb-3">
                                The web design course provided a solid foundation for me. The instructors were
                                knowledgeable and supportive, and the interactive learning environment was
                                engaging. I highly recommend it!
                            </p>

                            <div className="d-flex align-items-center justify-content-between">
                                <div className="d-flex align-items-center">
                                    <img
                                        src="https://i.imgur.com/7k12EPD.png"
                                        alt="Sarah L"
                                        className="rounded-circle me-3"
                                        width="48"
                                        height="48"
                                    />
                                    <p className="fw-semibold mb-0">Marko M</p>
                                </div>
                                <button className="btn btn-outline-secondary btn-sm">Read More</button>
                            </div>
                        </div>
                    </div>

                    {/* Form */}
                    <div className="col-md-6">
                        <h2 className="fw-bold mb-2">Sign In</h2>
                        <p className="mb-4 text-muted">We'll be happy to see you!</p>

                        {error && <div className="alert alert-danger">{error}</div>}
                        {success && <div className="alert alert-success">{success}</div>}

                        <form onSubmit={handleSubmit}>
                            <div className="mb-3">
                                <label className="form-label">Email</label>
                                <input
                                    type="email"
                                    name="email"
                                    className="form-control"
                                    placeholder="Enter your Email"
                                    value={formData.email}
                                    onChange={handleChange}
                                />
                            </div>

                            <div className="mb-3">
                                <label className="form-label">Password</label>
                                <input
                                    type="password"
                                    name="password"
                                    className="form-control"
                                    placeholder="Enter your Password"
                                    value={formData.password}
                                    onChange={handleChange}
                                />
                            </div>

                            <button type="submit" className="btn btn-custom-color w-100 text-white mb-3">Login</button>
                            <p className="text-center mt-4">
                                Don't have an account? <a href="/home">Sign Up</a> <span>↗</span>
                            </p>

                        </form>
                    </div>
                </div>
            </div>

            <Footer/>
        </>
    );
}

export default Login;


