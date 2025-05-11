import React from 'react';
import TakenTestCard from "./TakenTestCard";
import Navbar from "../Navbar";
import Footer from "../Footer";

function TakenTests({courseData}) {
    return (
        <>
            <Navbar/>
            <div className="container mt-5">
                {courseData.map((course, index) => (
                    <div key={index} className="mb-5">
                        <h3 className="mb-4">{course.title}</h3>
                        <hr className="mt-0 mb-4"/>
                        <div className="row bg-light p-4 rounded-3">
                            {course.tasks.map((test, idx) => (
                                <TakenTestCard key={idx} index={idx} {...test} />
                            ))}
                        </div>
                    </div>
                ))}
            </div>
            <Footer/>
        </>
    );
}

export default TakenTests;