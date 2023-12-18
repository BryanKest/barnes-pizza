package be.vives.ti.barnespizza.responses;

import be.vives.ti.barnespizza.domain.Pizza;

import java.util.Optional;

public class PizzaOrderResponse {

    private Integer id;
    private String name;
    private Double price;
    private Optional<String> size;
    private String description;
    private Optional<Integer> amount;

    public PizzaOrderResponse(Pizza pizza, Integer amount) {
        this.id = pizza.getId();
        this.name = pizza.getName();
        this.price = pizza.getPrice();
        if(pizza.getSize() != null)
        {
            this.size = Optional.of(pizza.getSize().toString());
        }
        else {
            this.size = Optional.empty();
        }
        this.description = pizza.getDescription();
        this.amount = Optional.of(amount);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Optional<String> getSize() {
        return size;
    }

    public String getDescription() {
        return description;
    }

    public Optional<Integer> getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = Optional.of(amount);
    }

}
