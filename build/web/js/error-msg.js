// Get the error parameter from the URL
const urlParams = new URLSearchParams(window.location.search);
const div_message = urlParams.get('div_message');
const errorMessageDiv = document.getElementById('div_message');

if (div_message) {
    switch (div_message) {
        case 'invalid':
            errorMessageDiv.textContent = 'Invalid username or password.';
            break;
        case 'empty':
            errorMessageDiv.textContent = 'Please fill in all fields.';
            break;
        case 'notfound':
            errorMessageDiv.textContent = 'User not found. Please try again.';
            break;
        case 'server':
            errorMessageDiv.textContent = 'A server error occurred. Please try again later.';
            break;
        case 'logout':
            errorMessageDiv.textContent = 'You have been logged out. Please log in again.';
            break;
        case 'changesuccess':
            errorMessageDiv.textContent = 'Changes successfully saved.';
            break;
        case 'deletesuccess':
            errorMessageDiv.textContent = 'User successfully deleted.';
            break;
        case 'operationfailed':
            errorMessageDiv.textContent = 'Operation failed. Please try again.';
            break;
        default:
            errorMessageDiv.textContent = 'An error occurred. Please try again.';
    }
}