package be.vives.ti.barnespizza.requests;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class OrderCreateRequest {

    @NotEmpty(message = "Account id must not be empty")
    private Integer accountId;

    private List<PizzaOrderItemRequest> pizzas;

    private List<BeverageOrderItemRequest> beverages;

    @NotEmpty(message = "Address must not be empty")
    private String address;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public List<PizzaOrderItemRequest> getPizzas() {
        return pizzas;
    }

    public void setPizzas(List<PizzaOrderItemRequest> pizzas) {
        this.pizzas = pizzas;
    }

    public List<BeverageOrderItemRequest> getBeverages() {
        return beverages;
    }

    public void setBeverages(List<BeverageOrderItemRequest> beverages) {
        this.beverages = beverages;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

