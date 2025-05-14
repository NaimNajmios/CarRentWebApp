import Database.AdminOperation;
import User.Admin;
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
        maxRequestSize = 1024 * 1024 * 10) // 10MB
public class UpdateAdminServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UpdateAdminServlet.class.getName());
    private final AdminOperation adminOperation = new AdminOperation();
    private final Admin admin = new Admin();
    private static final String UPLOAD_DIRECTORY = "images/profile/"; // Relative path for storage in database and web
                                                                      // context

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        LOGGER.log(Level.INFO, "Processing admin profile update request at {0}",
                new java.util.Date().toString());

        // Retrieve all parameters from the form
        String adminID = request.getParameter("adminID");
        String userID = request.getParameter("userID");
        String adminName = request.getParameter("name");
        String adminEmail = request.getParameter("email");

        LOGGER.log(Level.FINE, "Received admin details - AdminID: {0}, UserID: {1}, Name: {2}, Email: {3}",
                new Object[] { adminID, userID, adminName, adminEmail });

        String profilePicturePath = null;
        Part profilePicturePart = null;
        try {
            profilePicturePart = request.getPart("profilePicture");
        } catch (ServletException e) {
            LOGGER.log(Level.WARNING, "Error retrieving profile picture part: {0}", e.getMessage());
            request.getSession().setAttribute("error", "Failed to process profile picture. Please try again.");
            response.sendRedirect(request.getContextPath() + "/admin/admin-profile.jsp");
            return;
        }

        if (profilePicturePart != null && profilePicturePart.getSize() > 0) {
            // Check file size explicitly (though @MultipartConfig enforces this)
            if (profilePicturePart.getSize() > 1024 * 1024 * 5) {
                LOGGER.log(Level.WARNING, "Profile picture exceeds maximum size of 5MB for adminID: {0}", adminID);
                request.getSession().setAttribute("error", "Profile picture exceeds maximum size of 5MB.");
                response.sendRedirect(request.getContextPath() + "/admin/admin-profile.jsp");
                return;
            }

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
                LOGGER.log(Level.WARNING, "ServletContext.getRealPath() returned null, using fallback path: {0}",
                        baseUploadPath);
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
                boolean updateProfilePictureSuccess = adminOperation.updateAdminProfilePicture(adminID,
                        profilePicturePath);
                LOGGER.log(Level.INFO, "Profile picture uploaded successfully to: {0}", uploadFile.getAbsolutePath());
                if (uploadFile.exists()) {
                    LOGGER.log(Level.INFO, "Confirmed: Profile picture file exists at: {0}",
                            uploadFile.getAbsolutePath());
                } else {
                    LOGGER.log(Level.SEVERE, "Profile picture file does not exist after writing: {0}",
                            uploadFile.getAbsolutePath());
                    profilePicturePath = null;
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error writing profile picture to disk: {0}", e.getMessage());
                profilePicturePath = null; // Indicate upload failure
            }
        } else {
            LOGGER.log(Level.INFO, "No new profile picture uploaded or file is empty.");
            // Retrieve the existing profile picture path to avoid overwriting with null
            String existingPath = adminOperation.getAdminProfilePicture(adminID); // Assumes this method exists in
                                                                                  // AdminOperation
            if (existingPath != null && !existingPath.isEmpty()) {
                profilePicturePath = existingPath;
            } else {
                profilePicturePath = "/images/profile/default_profile.jpg"; // Default path if none exists
            }
        }

        LOGGER.log(Level.INFO, "Updating admin ID: {0} with new details and picture path: {1}",
                new Object[] { adminID, profilePicturePath });

        // Set the updated admin details in the admin object
        admin.setAdminID(adminID);
        admin.setUserID(userID);
        admin.setName(adminName);
        admin.setEmail(adminEmail);
        admin.setProfileImagePath(profilePicturePath);

        LOGGER.log(Level.FINE, "Admin object to be updated: {0}", admin);

        // Update the admin details in the database
        boolean updated = adminOperation.updateAdminDetails(admin);

        if (updated) {
            LOGGER.log(Level.INFO, "Admin details updated successfully for ID: {0}", adminID);
            request.getSession().setAttribute("message", "Profile updated successfully.");
            // Update the session with the new admin details
            request.getSession().setAttribute("admin", admin);
            response.sendRedirect(request.getContextPath() + "/admin/admin-profile.jsp");
        } else {
            LOGGER.log(Level.WARNING, "Failed to update admin details for ID: {0}", adminID);
            request.getSession().setAttribute("error", "Failed to Ad profile. Please try again.");
            response.sendRedirect(request.getContextPath() + "/admin/admin-profile.jsp");
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
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
        return "Handles admin profile update functionality.";
    }
}