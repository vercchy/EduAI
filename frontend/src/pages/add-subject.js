// TODO: Dodaj slikicki mali za footerot (telefon,email,etc)

import React, {useState} from 'react';
import api from "../api/axios";
import reportWebVitals from '../reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../index.css';
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import { useNavigate } from 'react-router-dom';

reportWebVitals();

function AddSubject() {
    const [formData, setFormData] = useState({
        name: '',
        description: '',
        difficultyLevel: ''
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const navigate = useNavigate();

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
            const dataToSend = {
                ...formData,
                difficultyLevel: parseInt(formData.difficultyLevel)
            };

            const token = localStorage.getItem('token');

            const response = await api.post('/api/subjects', formData, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            setSuccess('Subject created successfully!');
            console.log('Created:', response.data);

            setTimeout(() => {
                navigate('/dashboard');
            }, 1000);

        } catch (err) {
            setError(err.response?.data?.message || 'Failed to create subject');
        }
    };
    return (
        <>
            <Navbar/>

            <div className="container my-4">
                <h2 className="mb-3">Create a New Subject</h2>
                {error && <div className="alert alert-danger">{error}</div>}
                {success && <div className="alert alert-success">{success}</div>}
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <label htmlFor="name" className="form-label">Subject Name *</label>
                        <input
                            type="text"
                            className="form-control"
                            id="name"
                            name="name"
                            placeholder="Enter the subject name"
                            value={formData.name}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="description" className="form-label">Description</label>
                        <textarea
                            className="form-control"
                            id="description"
                            name="description"
                            placeholder="Enter the description"
                            rows="3"
                            value={formData.description}
                            onChange={handleChange}
                        />
                    </div>
                    <div className="mb-3">
                        <label htmlFor="difficultyLevel" className="form-label">Difficulty Level</label>
                        <select
                            className="form-control"
                            id="difficultyLevel"
                            name="difficultyLevel"
                            value={formData.difficultyLevel}
                            onChange={handleChange}
                        >
                            <option value="1">Easy</option>
                            <option value="2">Medium</option>
                            <option value="3">Hard</option>
                        </select>
                    </div>
                    <button type="submit" className="btn btn-primary">Create Subject</button>
                </form>

            </div>


            <Footer/>
        </>
    );
}



export default AddSubject;


