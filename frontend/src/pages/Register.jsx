import React, { useState } from 'react';
import api from "../api/axios";
import zhurka_slika from '../assets/zhurkaaa.svg';
import '../index.css';
import 'bootstrap/dist/css/bootstrap.min.css';

function RegisterForm() {
    const [formData, setFormData] = useState({
        fullName: '',
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
            const [firstName, ...rest] = formData.fullName.trim().split(' ');
            const lastName = rest.join(' ');

            const response = await api.post('/auth/register', {
                firstName,
                lastName,
                email: formData.email,
                password: formData.password
            });

            setSuccess(response.data);
        } catch (err) {
            setError(err.response?.data || 'Registration failed');
        }
    };

    return (
        <div className="container my-5">
            <div className="row align-items-center">
                <div className="col-md-6">
                    <h2 className="fw-bold mb-2">Sign Up</h2>
                    <p className="mb-4 text-muted">Create an account for free.</p>

                    {error && <div className="alert alert-danger">{error}</div>}
                    {success && <div className="alert alert-success">{success}</div>}

                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label className="form-label">Full Name</label>
                            <input
                                type="text"
                                name="fullName"
                                className="form-control"
                                placeholder="Enter your Name and Surname"
                                value={formData.fullName}
                                onChange={handleChange}
                                required
                            />
                        </div>

                        <div className="mb-3">
                            <label className="form-label">Email</label>
                            <input
                                type="email"
                                name="email"
                                className="form-control"
                                placeholder="Enter your Email"
                                value={formData.email}
                                onChange={handleChange}
                                required
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
                                required
                            />
                        </div>

                        <button type="submit" className="btn btn-custom-color w-100 text-white mb-3">Sign Up</button>
                        <p className="text-center mt-4">
                            Already have an account? <a href="/login">Login</a> <span>↗</span>
                        </p>
                    </form>
                </div>

                <div className="col-md-6 text-center">
                    <img src={zhurka_slika} alt="Zhurka" width="500" height="500" />
                </div>
            </div>
        </div>
    );
}

export default RegisterForm;
