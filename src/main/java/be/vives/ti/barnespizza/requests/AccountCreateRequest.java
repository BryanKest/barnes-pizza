package be.vives.ti.barnespizza.requests;

import jakarta.validation.constraints.*;

public class AccountCreateRequest {

    @NotNull
    @Email
    @Size(min = 9, max = 50, message = "Email must be minimum 9 characters, and maximum 50 characters long")
    private String email;

    @NotNull
    @Size(min = 9, max = 50, message = "Password must be between 9 and 50 characters long")
    @Pattern.List({
            @Pattern(regexp = "(?=.*[0-9]).+", message = "Password must contain at least one digit"),
            @Pattern(regexp = "(?=.*[a-z]).+", message = "Password must contain at least one lowercase letter"),
            @Pattern(regexp = "(?=.*[A-Z]).+", message = "Password must contain at least one uppercase letter"),
            @Pattern(regexp = "(?=.*[@#$%^&+=]).+", message = "Password must contain at least one special character"),
            @Pattern(regexp = "\\S+", message = "Password must not contain spaces")
    })
    private String password;

    @NotNull
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters long")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "First name must only contain letters")
    private String fname;

    @NotNull
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters long")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Last name must only contain letters")
    private String name;

    @NotNull
    @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters long")
    private String address;

    @NotNull
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}