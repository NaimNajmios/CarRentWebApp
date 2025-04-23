function confirmDelete() {
    // First confirmation
    if (confirm('Are you sure you want to delete this user?')) {
        // Second confirmation
        return confirm('This action cannot be undone. Confirm deletion?');
    }
    return false; // Cancel if first confirmation is declined
}