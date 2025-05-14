
import Database.DBOperation;
import User.Client;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Naim Najmi
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024, // 1MB
        maxFileSize = 1024 * 1024 * 5, // 5MB
        maxRequestSize = 1024 * 1024 * 10)   // 10MB

public class ClientProfileUpdateServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ClientProfileUpdateServlet.class.getName());
    private final DBOperation dbo = new DBOperation();
    private final Client client = new Client();
    private static final String UPLOAD_DIRECTORY = "images/profile/"; // Relative path for storage in database and web context

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOGGER.log(Level.INFO, "Processing client profile update request at {0}",
                new java.util.Date().toString());

        // Retrieve all parameters from the form
        String clientID = request.getParameter("clientID");
        String clientUsername = request.getParameter("name");
        String clientEmail = request.getParameter("email");
        String clientRole = request.getParameter("role");
        String clientPhone = request.getParameter("phone");
        String clientAddress = request.getParameter("address");

        LOGGER.log(Level.FINE, "Received client details - ID: {0}, Username: {1}, Email: {2}, Role: {3}, Phone: {4}, Address: {5}",
                new Object[]{clientID, clientUsername, clientEmail, clientRole, clientPhone, clientAddress});

        String profilePicturePath = null;
        Part profilePicturePart = null;
        try {
            profilePicturePart = request.getPart("profilePicture");
        } catch (ServletException e) {
            LOGGER.log(Level.WARNING, "Error retrieving profile picture part: {0}", e.getMessage());
        }

        if (profilePicturePart != null && profilePicturePart.getSize() > 0) {
            String originalFileName = profilePicturePart.getSubmittedFileName();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            // Dynamically determine the base upload path using ServletContext.getRealPath()
            String baseUploadPath = getServletContext().getRealPath("/");
            if (baseUploadPath == null) {
                // Fallback to a system property or default path if getRealPath() returns null
                baseUploadPath = System.getProperty("upload.path", System.getProperty("user.home") + "/app/uploads/");
                LOGGER.log(Level.WARNING, "ServletContext.getRealPath() returned null, using fallback path: {0}", baseUploadPath);
            }
            String uploadPath = baseUploadPath + UPLOAD_DIRECTORY;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                boolean dirCreated = uploadDir.mkdirs();
                if (dirCreated) {
                    LOGGER.log(Level.INFO, "Created upload directory: {0}", uploadPath);
                } else {
                    LOGGER.log(Level.SEVERE, "Failed to create upload directory: {0}. Check permissions.", uploadPath);
                    throw new IOException("Unable to create upload directory: " + uploadPath);
                }
            }

            // Check if the directory is writable
            if (!uploadDir.canWrite()) {
                LOGGER.log(Level.SEVERE, "Upload directory is not writable: {0}. Check permissions.", uploadPath);
                throw new IOException("Upload directory is not writable: " + uploadPath);
            }

            String randomFileName;
            File uploadFile;
            do {
                randomFileName = System.currentTimeMillis() + "_" + Math.round(Math.random() * 1000) + extension;
                uploadFile = new File(uploadPath + randomFileName);
            } while (uploadFile.exists());

            profilePicturePath = "/" + UPLOAD_DIRECTORY + randomFileName; // Store relative path in DB
            try {
                profilePicturePart.write(uploadFile.getAbsolutePath());
                LOGGER.log(Level.INFO, "Profile picture uploaded successfully to: {0}", uploadFile.getAbsolutePath());
                if (uploadFile.exists()) {
                    LOGGER.log(Level.INFO, "Confirmed: Profile picture file exists at: {0}", uploadFile.getAbsolutePath());
                } else {
                    LOGGER.log(Level.SEVERE, "Profile picture file does not exist after writing: {0}", uploadFile.getAbsolutePath());
                    profilePicturePath = null;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error writing profile picture to disk: {0}", e.getMessage());
                profilePicturePath = null; // Indicate upload failure
            }
        } else {
            LOGGER.log(Level.INFO, "No new profile picture uploaded or file is empty.");
            // Retrieve the existing profile picture path to avoid overwriting with null
            String existingPath = dbo.getClientProfilePicture(clientID); // Assumes this method exists in DBOperation
            if (existingPath != null && !existingPath.isEmpty()) {
                profilePicturePath = existingPath;
            } else {
                profilePicturePath = "/images/profile/default_profile.jpg"; // Default path if none exists
            }
        }

        LOGGER.log(Level.INFO, "Updating client ID: {0} with new details and picture path: {1}",
                new Object[]{clientID, profilePicturePath});

        // Set the updated user details in the client object
        client.setUserID(clientID);
        client.setName(clientUsername);
        client.setEmail(clientEmail);
        client.setPhoneNumber(clientPhone);
        client.setAddress(clientAddress);
        client.setProfileImagePath(profilePicturePath);

        LOGGER.log(Level.FINE, "Client object to be updated: {0}", client);

        // Update the user details in the database
        boolean updateDetailsSuccess = dbo.updateClientDetails(client);
        boolean updateProfilePictureSuccess = true; // Assume success if no new picture

        // Update profile picture path only if a new picture was uploaded
        if (profilePicturePart != null && profilePicturePart.getSize() > 0 && profilePicturePath != null) {
            updateProfilePictureSuccess = dbo.updateClientProfilePicture(clientID, profilePicturePath);
        }

        if (updateDetailsSuccess && updateProfilePictureSuccess) {
            LOGGER.log(Level.INFO, "Client details updated successfully for ID: {0}", clientID);
            request.getSession().setAttribute("message", "Profile updated successfully.");
            response.sendRedirect(request.getContextPath() + "/client/profile.jsp");
        } else {
            LOGGER.log(Level.WARNING, "Failed to update client details for ID: {0}. Details update: {1}, Picture update: {2}",
                    new Object[]{clientID, updateDetailsSuccess, updateProfilePictureSuccess});
            request.getSession().setAttribute("error", "Failed to update profile. Please try again.");
            response.sendRedirect(request.getContextPath() + "/client/profile.jsp");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles client profile update functionality.";
    }
}
