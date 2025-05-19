import React, { useState } from 'react';
import '../Contact.css';
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";

const Contact = () => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        message: ''
    });

    const [submitted, setSubmitted] = useState(false);

    const handleChange = e => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch("https://formspree.io/f/mldbvjrq", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Accept: "application/json",
                },
                body: JSON.stringify(formData),
            });

            if (response.ok) {
                setSubmitted(true);
                setFormData({ name: '', email: '', message: '' });
            } else {
                alert("Something went wrong. Please try again.");
            }
        } catch (error) {
            alert("Error submitting the form.");
        }
    };

    return (
        <>
            <Navbar />
            <div className="contact-container">
                <div className="contact-content">
                    <h1 className="contact-title">Contact Us</h1>
                    <p className="contact-subtitle">We’d love to hear from you!</p>

                    {submitted ? (
                        <p className="thank-you-message">Thank you for reaching out! We’ll get back to you soon.</p>
                    ) : (
                        <form className="contact-form" onSubmit={handleSubmit}>
                            <div className="form-group">
                                <label htmlFor="name">Name</label>
                                <input
                                    type="text"
                                    id="name"
                                    name="name"
                                    value={formData.name}
                                    onChange={handleChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="email">Email</label>
                                <input
                                    type="email"
                                    id="email"
                                    name="email"
                                    value={formData.email}
                                    onChange={handleChange}
                                    required
                                />
                            </div>

                            <div className="form-group">
                                <label htmlFor="message">Message</label>
                                <textarea
                                    id="message"
                                    name="message"
                                    rows="5"
                                    value={formData.message}
                                    onChange={handleChange}
                                    required
                                ></textarea>
                            </div>

                            <button type="submit" className="submit-button">Send Message</button>
                        </form>
                    )}
                </div>
            </div>
            <Footer />
        </>
    );
};

export default Contact;
