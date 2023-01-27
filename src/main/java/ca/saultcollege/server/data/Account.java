package ca.saultcollege.server.data;

public class Account {
    private String FirstName;
    private String LastName;
    private String Email;
    private String Password;
    private String ConfirmPassword;
    private String Role;

    public Account() {
        this.FirstName = "";
        this.LastName = "";
        this.Email = "";
        this.Password = "";
        this.Role = "";
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
