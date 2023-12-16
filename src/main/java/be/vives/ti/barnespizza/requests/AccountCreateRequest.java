package be.vives.ti.barnespizza.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class AccountCreateRequest {

    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 9, max = 50, message = "Password must be between 9 and 50 characters")
    private String password;

    @NotEmpty(message = "First name must not be empty")
    private String fname;

    @NotEmpty(message = "Last name must not be empty")
    private String name;

    @NotEmpty(message = "Address must not be empty")
    private String address;

    @NotEmpty(message = "Phone must not be empty")
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