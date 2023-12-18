package be.vives.ti.barnespizza.responses;

import be.vives.ti.barnespizza.domain.Beverage;

public class BeverageOrderResponse {

    private Integer id;
    private String name;
    private Double price;
    private Integer amount;

    public BeverageOrderResponse(Beverage beverage, Integer amount) {
        this.id = beverage.getId();
        this.name = beverage.getName();
        this.price = beverage.getPrice();
        this.amount = amount;
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

    public Integer getAmount() {
        return amount;
    }
}
