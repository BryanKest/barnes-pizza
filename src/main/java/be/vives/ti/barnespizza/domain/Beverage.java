package be.vives.ti.barnespizza.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Entity
@Table(name = "beverages")
public class Beverage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive value")
    private Double price;


    @OneToMany(mappedBy = "beverage", cascade = CascadeType.ALL)
    private List<BeverageOrderItem> beverageOrderItems;
    protected Beverage(){

    }

    public Beverage(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
    public List<BeverageOrderItem> getBeverageOrderItems() {
        return beverageOrderItems;
    }

    public void setBeverageOrderItems(List<BeverageOrderItem> beverageOrderItems) {
        this.beverageOrderItems = beverageOrderItems;
    }
}
