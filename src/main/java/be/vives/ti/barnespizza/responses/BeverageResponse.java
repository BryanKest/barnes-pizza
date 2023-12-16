package be.vives.ti.barnespizza.responses;

import be.vives.ti.barnespizza.domain.Beverage;

public class BeverageResponse {

    private Integer id;
    private String name;
    private Double price;

    public BeverageResponse(Beverage beverage) {
        this.id = beverage.getId();
        this.name = beverage.getName();
        this.price = beverage.getPrice();
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
}
