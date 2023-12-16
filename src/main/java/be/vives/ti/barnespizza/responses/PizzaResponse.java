package be.vives.ti.barnespizza.responses;

import be.vives.ti.barnespizza.domain.Pizza;

import java.util.Optional;

public class PizzaResponse {

    private Integer id;
    private String name;
    private Double price;
    private Optional<String> size;
    private String description;

    public PizzaResponse(Pizza pizza) {
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

}
