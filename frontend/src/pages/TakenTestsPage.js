import React, { useEffect, useState } from "react";
import TakenTests from "../components/TakenTests/TakenTests";


function TakenTestsContainer() {
    const [courseData, setCourses] = useState([]);

    useEffect(() => {
        setCourses([
            {
                title: "Front End Web Development",
                tasks: [
                    { id: 1, title: "HTML Structure Test", description: "Given a plain text description..." },
                    { id: 2, title: "CSS Styling Test", description: "Style a button..." },
                    { id: 3, title: "JavaScript Basics Test", description: "Write a script to display..." },
                    { id: 4, title: "JavaScript DOM Manipulation Test", description: "Change text inside a <p>..." },
                    { id: 5, title: "Responsive Design Test", description: "Use CSS media queries..." },
                    { id: 6, title: "Debugging & Fixing Code Test", description: "Ensure students understand..." }
                ]
            },
            {
                title: "Front End Web Development 2",
                tasks: [
                    { id: 1, title: "HTML Structure Test", description: "Given a plain text description..." },
                    { id: 2, title: "CSS Styling Test", description: "Style a button..." },
                    { id: 3, title: "JavaScript Basics Test", description: "Write a script to display..." },
                    { id: 4, title: "JavaScript DOM Manipulation Test", description: "Change text inside a <p>..." },
                    { id: 5, title: "Responsive Design Test", description: "Use CSS media queries..." },
                    { id: 6, title: "Debugging & Fixing Code Test", description: "Ensure students understand..." }
                ]
            }
        ]);
        //
        // fetch("/api/taken-tests")
        //     .then(res => res.json())
        //     .then(data => setTasks(data))
        //     .catch(err => console.error("Error loading tests:", err));
    }, []);

    return <TakenTests title="Taken Tests" courseData={courseData} />;
}

export default TakenTestsContainer;