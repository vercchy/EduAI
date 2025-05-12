import React, { useState } from 'react';
import ReactDOM from 'react-dom/client';
import reportWebVitals from '../reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../index.css';
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";





// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

function TestInProgress() {
    const [essayAnswer, setEssayAnswer] = useState('');

    const handleEssayChange = (e) => {
        setEssayAnswer(e.target.value)
    };
    return (
        <>
            <Navbar/>
            <div className="container my-5">
                <h2 className="mt-4">Front-End Web Development - Test 7</h2>
                <div className="text-end text-danger mb-3">Time remaining: 00:23:33</div>

                {/* Question 1 - Single Choice */}
                <div className="mb-4">
                    <h5>Question 1:</h5>
                    <p>Which of the following is a correct use of &lt;span&gt;? (select one)</p>
                    <div>
                        <label><input type="radio" name="q1" /> Creating a full-page layout with multiple sections</label><br />
                        <label><input type="radio" name="q1" /> Placing block elements like &lt;div&gt; inside it</label><br />
                        <label><input type="radio" name="q1" /> Using it as a clickable button instead of &lt;button&gt;</label><br />
                        <label><input type="radio" name="q1" /> Wrapping a word inside a paragraph to apply a different style</label>
                    </div>
                </div>

                {/* Question 2 - Multi Select */}
                <div className="mb-4">
                    <h5>Question 2:</h5>
                    <p>Which of the following are correct uses of &lt;div&gt;? (multiple choice)</p>
                    <div>
                        <label><input type="checkbox" /> Grouping elements for styling purposes</label><br />
                        <label><input type="checkbox" /> Using &lt;div&gt; instead of a &lt;button&gt; for interactivity</label><br />
                        <label><input type="checkbox" /> Creating a structured layout for a webpage</label><br />
                        <label><input type="checkbox" /> Placing a &lt;div&gt; inside a &lt;span&gt;</label>
                    </div>
                </div>

                {/* Question 3 - Essay */}
                <div className="mb-4">
                    <h5>Question 3:</h5>
                    <p>Is the code in the picture correctly written?</p>
                    <textarea
                        className="form-control"
                        rows="5"
                        placeholder="Write your answer here..."
                        value={essayAnswer}
                        onChange={handleEssayChange}
                    />
                </div>

                {/* Submit Button */}
                <button className="btn btn-primary">Submit</button>
            </div>

            <Footer/>
        </>
    );
}

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <React.StrictMode>
        <TestInProgress />
    </React.StrictMode>
);

reportWebVitals();

export default TestInProgress;



