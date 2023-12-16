package be.vives.ti.barnespizza.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<PizzaOrderItem> pizzaOrderItems;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<BeverageOrderItem> beverageOrderItems;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderTime;

    private String address;

    private Double totalPrice;

    protected Order(){

    }

    public Order(Account account, Date orderTime, String address, Double totalPrice) {
        this.account = account;
        this.orderTime = orderTime;
        this.address = address;
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<PizzaOrderItem> getPizzaOrderItems() {
        return pizzaOrderItems;
    }

    public void setPizzaOrderItems(List<PizzaOrderItem> pizzaOrderItems) {
        this.pizzaOrderItems = pizzaOrderItems;
    }

    public List<BeverageOrderItem> getBeverageOrderItems() {
        return beverageOrderItems;
    }

    public void setBeverageOrderItems(List<BeverageOrderItem> beverageOrderItems) {
        this.beverageOrderItems = beverageOrderItems;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }


}
