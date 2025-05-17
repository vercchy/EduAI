import React from 'react';
import logo from '../assets/eduAI_logo.svg';

function Navbar() {
    const token = localStorage.getItem('token');
    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-white shadow-sm px-4">
            <a className="navbar-brand" href="/home">
                <img src={logo} alt="EduAI Logo" width="80" height="80" />
            </a>
            <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
                <span className="navbar-toggler-icon"></span>
            </button>
            <div className="collapse navbar-collapse" id="navbarContent">
                <ul className="navbar-nav ms-4">
                    <li className="nav-item"><a className="nav-link" href="/home">Home</a></li>
                    <li className="nav-item"><a className="nav-link" href="#">Courses</a></li>
                    <li className="nav-item"><a className="nav-link" href="/upcoming-tests">Upcoming Tests</a></li>
                    <li className="nav-item"><a className="nav-link" href="/taken-tests">Taken Tests</a></li>
                    <li className="nav-item"><a className="nav-link" href="#">About Us</a></li>
                    <li className="nav-item"><a className="nav-link" href="#">Contact</a></li>
                </ul>
                {!token && (
                    <div className="ms-auto">
                        <a className="btn btn-custom-color text-white ms-2" href="/home">Sign Up</a>
                        <a className="btn btn-custom-color text-white ms-2" href="/login">Login</a>
                    </div>
                )}
            </div>
        </nav>
    );
}

export default Navbar;