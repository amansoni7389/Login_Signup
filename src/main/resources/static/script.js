// Wait for the DOM to load
document.addEventListener('DOMContentLoaded', () => {

    // Handle signup form submission
    const signupForm = document.getElementById('signupForm');
    if (signupForm) {
        signupForm.addEventListener('submit', function (e) {
            e.preventDefault();

            const username = document.getElementById('username').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            // Call the signup API
            fetch('http://localhost:8080/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: username,
                    email: email,
                    password: password,
                })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Signup successful!');
                    window.location.href = '/login.html'; // Redirect to login page
                } else {
                    alert('Signup failed: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Something went wrong. Please try again later.');
            });
        });
    }

    // Handle login form submission
	const loginForm = document.getElementById('loginForm');
	if (loginForm) {
	    loginForm.addEventListener('submit', function (e) {
	        e.preventDefault();

	        const username = document.getElementById('loginUsername').value;
	        const password = document.getElementById('loginPassword').value;

	        // Call the login API
	        fetch('http://localhost:8080/auth/login', {
	            method: 'POST',
	            headers: {
	                'Content-Type': 'application/json',
	            },
	            body: JSON.stringify({
	                username: username,
	                password: password,
	            })
	        })
	        .then(response => {
	            const contentType = response.headers.get("Content-Type");
	            if (contentType && contentType.includes("application/json")) {
	                return response.json(); // Parse JSON if content-type is JSON
	            } else {
	                return response.text(); // Otherwise, treat the response as plain text
	            }
	        })
	        .then(data => {
	            if (typeof data === 'object' && data.success) {
	                localStorage.setItem('token', data.token); // Store token
	                alert('Login successful!');
	                window.location.href = '/welcome.html'; // Redirect to the welcome page
	            } else {
	                alert('Login failed: ' + data); // If data is plain text, show it directly
	            }
	        })
	        .catch(error => {
	            console.error('Error:', error);
	            alert('Something went wrong. Please try again later.');
	        });
	    });
	}



    // Handle forgot password form submission
    const forgotPasswordForm = document.getElementById('forgotPasswordForm');
    if (forgotPasswordForm) {
        forgotPasswordForm.addEventListener('submit', function (e) {
            e.preventDefault();

            const email = document.getElementById('email').value;

            // Call the forgot password API
            fetch('http://localhost:8080/forgot-password', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: email,
                })
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Password reset link sent! Please check your email.');
                    window.location.href = '/login.html'; // Redirect to login page
                } else {
                    alert('Error: ' + data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Something went wrong. Please try again later.');
            });
        });
    }

    // Handle reset password form submission
	const resetPasswordForm = document.getElementById('resetPasswordForm');
	        if (resetPasswordForm) {
	            resetPasswordForm.addEventListener('submit', function (e) {
	                e.preventDefault();

	                const token = document.getElementById('token').value;  // Getting token from hidden input
	                const newPassword = document.getElementById('newPassword').value;
	                const confirmPassword = document.getElementById('confirmPassword').value;

	                // Check if the passwords match
	                if (newPassword !== confirmPassword) {
	                    alert('Passwords do not match!');
	                    return;
	                }

	                // Call the reset password API
	                fetch('http://localhost:8080/reset-password?token=' + token + '&newPassword=' + newPassword, {
	                    method: 'POST',
	                    headers: {
	                        'Content-Type': 'application/json',
	                    },
	                    body: JSON.stringify({
	                        token: token,
	                        newPassword: newPassword
	                    })
	                })
	                .then(response => response.json())
	                .then(data => {
	                    if (data.success) {
	                        alert('Password successfully reset!');
	                        window.location.href = '/login'; // Redirect to login page
	                    } else {
	                        alert('Error resetting password: ' + data.message);
	                    }
	                })
	                .catch(error => {
	                    console.error('Error:', error);
	                    alert('Something went wrong. Please try again later.');
	                });
	            });
	        }

	});



