const Auth = {
    getUserRole() {
        return localStorage.getItem('role');
    },

    isProfessor() {
        return this.getUserRole() === 'ROLE_PROFESSOR';
    },

    isStudent() {
        return this.getUserRole() === 'ROLE_STUDENT';
    },

    getToken() {
        return localStorage.getItem('token');
    },

    isUserLoggedIn() {
        return !!this.getToken(); // stricter check: also rejects empty string
    }
};

export default Auth;