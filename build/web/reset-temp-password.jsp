<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Reset Password | CarRent</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <%-- Styling include --%>
        <%@ include file="../include/credential-styling.html" %>
    </head>
    <body>
        <div class="container mx-auto px-4">
            <div class="flex justify-center items-center min-h-screen">
                <div class="login-card w-full max-w-md p-8 bg-white rounded-lg shadow-md">
                    <!-- Logo -->
                    <div class="text-center mb-6">
                        <h1 class="text-3xl font-semibold text-gray-800">CarRent</h1>
                        <p class="text-gray-500 mt-2">Please set a new password to continue.</p>
                    </div>

                    <!-- Error Message Div -->
                    <div id="div_message" class="text-center mb-6 text-red-500"></div>

                    <!-- Password Reset Form -->
                    <form action="ResetTempPass" method="POST" class="space-y-6">
                        <div>
                            <label for="newPassword" class="block text-sm font-medium text-gray-700">New Password *</label>
                            <input type="password" id="newPassword" name="newPassword" placeholder="Enter new password"
                                   class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                        </div>

                        <div>
                            <label for="confirmPassword" class="block text-sm font-medium text-gray-700">Confirm Password *</label>
                            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm new password"
                                   class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                        </div>

                        <button type="submit" class="w-full btn-primary text-white py-2 rounded-lg font-medium">Save & Continue</button>
                    </form>
                </div>
            </div>
        </div>

        <%-- JS includes (e.g., validation, toast) --%>
        <%@ include file="../include/credential-js.html" %>
    </body>
</html>
