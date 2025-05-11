import React, { useEffect, useState } from "react";
import UpcomingTests from "../components/UpcomingTests/UpcomingTests";

function UpcomingTestsContainer() {
    const [courseData, setCourses] = useState([]);

    useEffect(() => {
        setCourses([
            {
                title: "Front End Web Development",
                tasks: [
                    { id: 1, title: "HTML Structure Test", description: "Given a plain text description...", date: "03.04.2025", time: "18:00" },
                    { id: 2, title: "CSS Styling Test", description: "Style a button...", date: "04.04.2025", time: "17:00" },
                    { id: 3, title: "JavaScript Basics Test", description: "Write a script to display...", date: "05.04.2025", time: "12:00" },
                    { id: 4, title: "JavaScript DOM Manipulation Test", description: "Change text inside a <p>...", date: "09.04.2025", time: "12:30" },
                    { id: 5, title: "Responsive Design Test", description: "Use CSS media queries...", date: "10.04.2025", time: "12:30" },
                    { id: 6, title: "Debugging & Fixing Code Test", description: "Ensure students understand...", date: "18.04.2025", time: "14:00" }
                ]
            },
            {
                title: "Front End Web Development 2",
                tasks: [
                    { id: 1, title: "HTML Structure Test", description: "Given a plain text description...", date: "03.04.2025", time: "18:00" },
                    { id: 2, title: "CSS Styling Test", description: "Style a button...", date: "04.04.2025", time: "17:00" },
                    { id: 3, title: "JavaScript Basics Test", description: "Write a script to display...", date: "05.04.2025", time: "12:00" },
                    { id: 4, title: "JavaScript DOM Manipulation Test", description: "Change text inside a <p>...", date: "09.04.2025", time: "12:30" },
                    { id: 5, title: "Responsive Design Test", description: "Use CSS media queries...", date: "10.04.2025", time: "12:30" },
                    { id: 6, title: "Debugging & Fixing Code Test", description: "Ensure students understand...", date: "18.04.2025", time: "14:00" }
                ]
            }
        ]);
        //
        // fetch("/api/upcoming-tests")
        //     .then(res => res.json())
        //     .then(data => setTasks(data))
        //     .catch(err => console.error("Error loading tests:", err));
    }, []);

    return <UpcomingTests title="Upcoming Tests" courseData={courseData} />;
}

export default UpcomingTestsContainer;