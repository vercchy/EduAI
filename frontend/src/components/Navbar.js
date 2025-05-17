import React from 'react';
import logo from '../assets/eduAI_logo.svg';
import Auth from "../utils/auth";

function Navbar() {
    const isUserLoggedIn = Auth.isUserLoggedIn();
    const isProfessor = Auth.isProfessor();
    const homeTabTitle = isUserLoggedIn ? 'Subjects' : 'Home';
    const testsTabTitle = isProfessor ? 'My Created Tests' : 'Assigned Tests';
    const takenTestsTabTitle = isProfessor ? 'Student Submissions' : 'Taken Tests';

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
                    <li className="nav-item"><a className="nav-link" href="/home">{homeTabTitle}</a></li>
                    {isUserLoggedIn && (
                        <>
                            <li className="nav-item">
                                <a className="nav-link" href="/upcoming-tests">{testsTabTitle}</a>
                            </li>
                            <li className="nav-item">
                                <a className="nav-link" href="/taken-tests">{takenTestsTabTitle}</a>
                            </li>
                        </>
                    )}
                    <li className="nav-item"><a className="nav-link" href="#">About Us</a></li>
                    <li className="nav-item"><a className="nav-link" href="#">Contact</a></li>
                </ul>
                <div className="ms-auto">
                    {!isUserLoggedIn ? (
                        <>
                            <a className="btn btn-custom-color text-white ms-2" href="/home">Sign Up</a>
                            <a className="btn btn-custom-color text-white ms-2" href="/login">Login</a>
                        </>
                    ) : (
                        <button className="btn btn-outline-danger ms-2" onClick={() => {
                            localStorage.removeItem('token');
                            localStorage.removeItem('role');
                            window.location.href = '/login';
                        }}>
                            Logout
                        </button>
                    )}
                </div>
            </div>
        </nav>
    );
}

export default Navbar;