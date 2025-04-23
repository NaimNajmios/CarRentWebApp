function includeHTML() {
    var elements = document.querySelectorAll('[data-include]');
    elements.forEach(function (el) {
        var file = el.getAttribute('data-include');
        fetch(file)
            .then(response => response.text())
            .then(data => {
                el.innerHTML = data;
                el.removeAttribute('data-include');
                // Re-run to handle nested includes
                includeHTML();
            })
            .catch(err => console.error('Error loading include:', err));
    });
}

document.addEventListener('DOMContentLoaded', includeHTML);