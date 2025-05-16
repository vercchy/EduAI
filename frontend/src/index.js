// TODO: Dodaj slikicki mali za footerot (telefon,email,etc)
import React, { useState } from 'react';
import axios from 'axios';
import ReactDOM from 'react-dom/client';
import {Routes, Route, BrowserRouter} from 'react-router-dom';
import reportWebVitals from './reportWebVitals';
import Login from './pages/Login';
import Student from "./pages/student";
import 'bootstrap/dist/css/bootstrap.min.css';
import './index.css';
import logo from './assets/eduAI_logo.svg';
import zhurka_slika from './assets/zhurkaaa.svg';
import UpcomingTestsPage from "./pages/UpcomingTestsPage";
import TakenTestsPage from "./pages/TakenTestsPage";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import TestInProgress from "./pages/TestInProgress";
import Teacher from "./pages/teacher";
import AddSubject from "./pages/add-subject";
import Subject from "./pages/subject";


// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals

reportWebVitals();



function App() {
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
            // Assume fullName = firstName + " " + lastName
            const [firstName, ...rest] = formData.fullName.split(' ');
            const lastName = rest.join(' ');

            const response = await axios.post('http://localhost:9090/auth/register', {
                firstName,
                lastName,
                email: formData.email,
                password: formData.password
            });

            setSuccess(response.data); // Example: "User registered successfully"
        } catch (err) {
            setError(err.response?.data || 'Registration failed');
        }
    };
    return (
        <>
            <Navbar/>

            {/* Sign Up Section */}
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
                                    placeholder="Enter your Name"
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
                            <div className="text-center mb-3 text-muted">OR</div>
                            <button type="button" className="btn btn-light w-100 border">
                                <img src="https://img.icons8.com/color/16/000000/google-logo.png" alt="google" className="me-2" />
                                Sign Up with Google
                            </button>

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

            <Footer/>
        </>
    );
}

// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//     <React.StrictMode>
//         <App />
//     </React.StrictMode>
// );

const token = localStorage.getItem('token');
if (token) {
    axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

export const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <BrowserRouter>
            <Routes>
                <Route path="/home" element={<App />} />
                <Route path="/login" element={<Login/>} />
                <Route path="/student" element={<Student/>} />
                <Route path="/upcoming-tests" element={<UpcomingTestsPage/>} />
                <Route path="/taken-tests" element={<TakenTestsPage/>} />
                <Route path="/TestInProgress" element={<TestInProgress/>} />
                <Route path="/teacher" element={<Teacher/>} />
                <Route path="/add-subject" element={<AddSubject/>} />
                <Route path="/subject/:id" element={<Subject/>} />
                {/* Add other routes here, e.g. Home */}
            </Routes>
        </BrowserRouter>
    </React.StrictMode>
);
export default App;


