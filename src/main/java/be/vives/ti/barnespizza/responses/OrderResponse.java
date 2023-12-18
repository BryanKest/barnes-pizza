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

    private Optional<List<PizzaOrderResponse>> pizzaOrderItems;

    private Optional<List<BeverageOrderResponse>> beverageOrderItems;

    private Date orderTime;

    private String address;

    private Double totalPrice;

    public OrderResponse(Integer id, Account account, List<PizzaOrderResponse> pizzaOrderItems, List<BeverageOrderResponse> beverageOrderItems, Date orderTime, String address, Double totalPrice) {
        this.id = id;
        this.account = account;
        this.pizzaOrderItems = Optional.ofNullable(pizzaOrderItems);
        this.beverageOrderItems = Optional.ofNullable(beverageOrderItems);
        this.orderTime = orderTime;
        this.address = address;
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public Optional<List<PizzaOrderResponse>> getPizzaOrderItems() {
        return pizzaOrderItems;
    }

    public Optional<List<BeverageOrderResponse>> getBeverageOrderItems() {
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
