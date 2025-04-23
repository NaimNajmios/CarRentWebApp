<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Login | CarRent</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <%-- Styling include --%>
        <%@ include file="../include/credential-styling.html" %>

    </head>
    <body>
        <div class="container mx-auto px-4">
            <div class="flex justify-center items-center min-h-screen">
                <div class="login-card w-full max-w-md p-8">
                    <!-- Logo -->
                    <div class="text-center mb-6">
                        <h1 class="text-3xl font-semibold text-gray-800">CarRent</h1>
                        <p class="text-gray-500 mt-2">Welcome back! Please log in to continue.</p>
                    </div>

                    <!-- Error Message Div -->
                    <div id="div_message" class="text-center mb-6 text-red-500"></div>

                    <!-- Login Form -->
                    <form action="Login" method="POST" class="space-y-6">
                        <div>
                            <label for="username" class="block text-sm font-medium text-gray-700">Username</label>
                            <input type="text" id="username" name="username" placeholder="Enter your username" 
                                   class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                        </div>

                        <div>
                            <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
                            <input type="password" id="password" name="password" placeholder="Enter your password" 
                                   class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500" required>
                        </div>

                        <div class="flex items-center justify-between">
                            <label class="flex items-center">
                                <input type="checkbox" checked class="h-4 w-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500">
                                <span class="ml-2 text-sm text-gray-600">Remember Me</span>
                            </label>
                            <a href="#" class="text-sm text-blue-600 hover:underline">Forgot Password?</a>
                        </div>

                        <button type="submit" class="w-full btn-primary text-white py-2 rounded-lg font-medium">Log In</button>
                    </form>

                    <!-- Sign Up Link -->
                    <div class="text-center mt-6">
                        <p class="text-sm text-gray-600">Don't have an account? 
                            <a href="signup.jsp" class="text-blue-600 hover:underline">Sign up</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <!-- JavaScript Dependencies -->
        <%@ include file="../include/credential-js.html" %>

    </body>
</html>