package Payment;

// Class to represent a payment in the database
public class Payment {
    
    // Variables
    private int paymentID;
    private String bookingID;
    private String paymentType;
    private double amount;
    private String paymentStatus;
    private String referenceNo;
    private String paymentDate;
    private String invoiceNumber;
    private String handledBy;
    private String proofOfPayment;

    // Empty constructor
    public Payment() {
    }

    // Constructor with parameters
    public Payment(int paymentID, String bookingID, String paymentType, double amount, String paymentStatus, String referenceNo, String paymentDate, String invoiceNumber, String handledBy, String proofOfPayment) {
        this.paymentID = paymentID;
        this.bookingID = bookingID;
        this.paymentType = paymentType;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.referenceNo = referenceNo;
        this.paymentDate = paymentDate;
        this.invoiceNumber = invoiceNumber;
        this.handledBy = handledBy;
        this.proofOfPayment = proofOfPayment;
    }

    // Getters and setters

    public int getPaymentID() {
        return this.paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public String getBookingID() {
        return this.bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getPaymentType() {
        return this.paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return this.paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getReferenceNo() {
        return this.referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getPaymentDate() {
        return this.paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getHandledBy() {
        return this.handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    public String getProofOfPayment() {
        return this.proofOfPayment;
    }

    public void setProofOfPayment(String proofOfPayment) {
        this.proofOfPayment = proofOfPayment;
    }

    @Override
    public String toString() {
        return "{" +
            " paymentID='" + getPaymentID() + "'" +
            ", bookingID='" + getBookingID() + "'" +
            ", paymentType='" + getPaymentType() + "'" +
            ", amount='" + getAmount() + "'" +
            ", paymentStatus='" + getPaymentStatus() + "'" +
            ", referenceNo='" + getReferenceNo() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", handledBy='" + getHandledBy() + "'" +
            ", proofOfPayment='" + getProofOfPayment() + "'" +
            "}";
    }

}
