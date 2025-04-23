<!DOCTYPE html>
<html lang="en">

<head>
    <title>Register | CarRent</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <%-- Styling include --%>
    <%@ include file="../include/credential-styling.html" %>

</head>

<body>
    <div class="container mx-auto px-4">
        <div class="flex justify-center items-center min-h-screen">
            <div class="register-card w-full max-w-md p-8">
                <!-- Logo -->
                <div class="text-center mb-6">
                    <h1 class="text-3xl font-semibold text-gray-800">CarRent</h1>
                    <p class="text-gray-500 mt-2">Create an account to get started.</p>
                </div>

                <!-- Message Divs -->
                <div id="message" class="text-center mb-6 text-green-500"></div>
                <div id="error-message" class="text-center mb-6 text-red-500"></div>

                <!-- Register Form -->
                <form action="Register" method="POST" class="space-y-6">
                    <div>
                        <label for="username" class="block text-sm font-medium text-gray-700">Username</label>
                        <input type="text" id="username" name="username" placeholder="Enter your username"
                            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required>
                    </div>

                    <div>
                        <label for="password" class="block text-sm font-medium text-gray-700">Password</label>
                        <input type="password" id="password" name="password" placeholder="Enter your password"
                            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required>
                    </div>

                    <div>
                        <label for="repeat-password" class="block text-sm font-medium text-gray-700">Repeat
                            Password</label>
                        <input type="password" id="repeat-password" name="repeat-password"
                            placeholder="Repeat your password"
                            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required>
                    </div>

                    <div>
                        <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
                        <input type="email" id="email" name="email" placeholder="Enter your email"
                            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required>
                    </div>

                    <div>
                        <label for="full-name" class="block text-sm font-medium text-gray-700">Full Name</label>
                        <input type="text" id="full-name" name="full-name" placeholder="Enter your full name"
                            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required>
                    </div>

                    <div>
                        <label for="address" class="block text-sm font-medium text-gray-700">Address</label>
                        <input type="text" id="address" name="address" placeholder="Enter your address"
                            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required>
                    </div>

                    <div>
                        <label for="phone-number" class="block text-sm font-medium text-gray-700">Phone Number</label>
                        <input type="text" id="phone-number" name="phone-number" placeholder="Enter your phone number"
                            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            required>
                    </div>

                    <button type="submit" class="w-full btn-primary text-white py-2 rounded-lg font-medium">Sign
                        Up</button>
                </form>

                <!-- Login Link -->
                <div class="text-center mt-6">
                    <p class="text-sm text-gray-600">Already have an account?
                        <a href="index.jsp" class="text-blue-600 hover:underline">Log in</a>
                    </p>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript Dependencies -->
    <%@ include file="../include/credential-js.html" %>

</body>

</html>