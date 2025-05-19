import React from 'react';

function Footer() {
    return (
        <footer className="bg-light py-5 mt-5">
            <div className="container">
                <div className="row justify-content-center text-center">
                    <div className="col-md-3 mb-4">
                        <h5>EduAI</h5>
                        <p><i className="bi bi-envelope"></i> hello@eduai.com</p>
                        <p><i className="bi bi-telephone"></i> +389 70 123 456</p>
                        <p><i className="bi bi-geo-alt"></i> Skopje</p>
                    </div>
                </div>
            </div>
        </footer>
    );
}

export default Footer;
