import React from 'react';

function Footer() {
    return (
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
    );
}

export default Footer;