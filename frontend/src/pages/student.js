// TODO: Dodaj slikicki mali za footerot (telefon,email,etc)

import React from 'react';
import ReactDOM from 'react-dom/client';
import reportWebVitals from '../reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../index.css';
import logo from '../assets/eduAI_logo.svg';
import grid1 from '../assets/grid1.png';
import grid2 from '../assets/grid2.png';
import grid3 from '../assets/grid3.png';
import zhurka_slika from '../assets/zhurkaaa.svg';



// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

function Student() {
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
            <div>
                <div className="h2 px-4 mb-2">Enrolled Courses </div>
                <div className="h6 px-4 mb-2">Mobile App Development</div>
                <div className="d-flex justify-content-between align-items-center px-4 mb-5">
                    <div className="me-3">
                        <p className="mb-0">
                            Dive into the world of mobile app development. Learn to build native iOS and Android applications using industry-leading frameworks like Swift and Kotlin.
                        </p>
                    </div>
                    <div>
                        <button className="btn btn-dark">View Course</button>
                    </div>
                </div>
                <div>
                    <div className="container">
                        <div className="row">
                            <div className="col">
                                <img src={grid1} alt="EduAI Logo" width="300" height="300"/>
                            </div>
                            <div className="col">
                                <img src={grid2} alt="EduAI Logo" width="300" height="300"/>
                            </div>
                            <div className="col">
                                <img src={grid3} alt="EduAI Logo" width="300" height="300"/>
                            </div>
                        </div>
                    </div>
                    <div className="d-flex justify-content-between align-items-center px-4 mb-5">
                        {/* Left side with two paragraphs */}
                        <div>
                            <p className="mb-1">8 Weeks</p>
                        </div>

                        {/* Right side with one paragraph */}
                        <div>
                            <p className="mb-0">By Ivan Ristovski</p>
                        </div>
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

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <Student />
    </React.StrictMode>
);

export default Student;


