package GettersAndSetters;

public class ClassMessages {

    private int messageId, customerId;
    private String message, dateTime, messageOwner, customerName;
    private byte[] picture;

    public ClassMessages(int messageId, int customerId, String message, String dateTime, String messageOwner) {
        this.messageId = messageId;
        this.customerId = customerId;
        this.message = message;
        this.dateTime = dateTime;
        this.messageOwner = messageOwner;
    }

    public ClassMessages() {
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime.replace("'", "''");
    }

    public String getMessageOwner() {
        return messageOwner;
    }

    public void setMessageOwner(String messageOwner) {
        this.messageOwner = messageOwner.replace("'", "''");
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
