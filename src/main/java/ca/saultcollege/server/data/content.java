package ca.saultcollege.server.data;

public class content {
    private String publicContent;
    private String staffContent;

    public content() {
        this.publicContent = "";
        this.staffContent = "";
    }

    public String getPublicContent() {
        return publicContent;
    }

    public void setPublicContent(String publicContent) {
        this.publicContent = publicContent;
    }

    public String getStaffContent() {
        return staffContent;
    }

    public void setStaffContent(String staffContent) {
        this.staffContent = staffContent;
    }
}
