import React from 'react';
import '../AboutUs.css';
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";

const AboutUs = () => {
    return (
        <>
        <Navbar/>
        <div className="about-container">
            <div className="about-content">
                <h1 className="about-title">About Us</h1>

                <p className="about-subtitle">
                    Empowering learning through smart assessment tools.
                </p>

                <div className="about-sections">
                    <div className="about-card">
                        <h2>Our Mission</h2>
                        <p>
                            We aim to simplify and enhance the test creation and evaluation process for professors, and offer students an intuitive way to take assessments.
                            By integrating AI and clean user design, we bridge the gap between traditional education and modern technology.
                        </p>
                    </div>

                    <div className="about-card">
                        <h2>Who We Are</h2>
                        <p>
                            We are a group of passionate students and developers building powerful tools for education. This platform was built as part of our final year project to demonstrate how web technologies and AI can improve classroom workflows.
                        </p>
                    </div>
                </div>

                <div className="about-tech">
                    <h2>Tech Stack</h2>
                    <p>React · Spring Boot · PostgreSQL · Docker · JWT Auth · Custom CSS · Llama</p>
                </div>
            </div>
        </div>
        <Footer/>
        </>
    );
};

export default AboutUs;
