package be.vives.ti.barnespizza.requests;

import jakarta.validation.constraints.NotEmpty;

public class PizzaOrderItemRequest {

    @NotEmpty(message = "Pizza id must not be empty")
    private Integer pizzaId;

    @NotEmpty(message = "Pizza size must not be empty")
    private String pizzaSize;

    @NotEmpty(message = "Amount must not be empty")
    private Integer amount;

    public Integer getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(Integer pizzaId) {
        this.pizzaId = pizzaId;
    }

    public String getPizzaSize() {
        return pizzaSize;
    }

    public void setPizzaSize(String pizzaSize) {
        this.pizzaSize = pizzaSize;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}

