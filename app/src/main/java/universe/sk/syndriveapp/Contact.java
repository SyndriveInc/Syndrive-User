package universe.sk.syndriveapp;

public class Contact {
    private String contactName, contactNumber;

    public Contact (String name, String number) {
        this.contactName = name;
        this.contactNumber = number;
    }


    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

}
