package be.vives.ti.barnespizza.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

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
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must only contain letters")
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

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Order> orders;

    protected Account(){

    }
    public Account(String email, String password, String fname, String name, String address, String phone){
        this.email = email;
        this.password = password;
        this.fname = fname;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Integer getId(){
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public void setAddress(String adress) {
        this.address = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
