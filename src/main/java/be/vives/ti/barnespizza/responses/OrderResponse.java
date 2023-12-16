package be.vives.ti.barnespizza.responses;

import be.vives.ti.barnespizza.domain.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class OrderResponse {
    private Integer id;

    private Account account;

    private List<PizzaOrderItem> pizzaOrderItems;

    private List<BeverageOrderItem> beverageOrderItems;

    private Date orderTime;

    private String address;

    private Double totalPrice;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.account = order.getAccount();
        this.pizzaOrderItems = order.getPizzaOrderItems();
        this.beverageOrderItems = order.getBeverageOrderItems();
        this.orderTime = order.getOrderTime();
        this.address = order.getAddress();
        this.totalPrice = order.getTotalPrice();
    }

    public Integer getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public List<PizzaOrderItem> getPizzaOrderItems() {
        return pizzaOrderItems;
    }

    public List<BeverageOrderItem> getBeverageOrderItems() {
        return beverageOrderItems;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public String getAddress() {
        return address;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

}
