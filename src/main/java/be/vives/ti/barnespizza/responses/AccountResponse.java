package be.vives.ti.barnespizza.responses;

import be.vives.ti.barnespizza.domain.Account;

public class AccountResponse {

    private Integer id;
    private String name;
    private String fname;
    private String email;
    private String password;
    private String address;
    private String phone;

    public AccountResponse(Account account){
        this.id = account.getId();
        this.name = account.getName();
        this.fname = account.getFname();
        this.email = account.getEmail();
        this.password = account.getPassword();
        this.address = account.getAddress();
        this.phone = account.getPhone();
    }

    public Integer getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFname() {
        return fname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }
}