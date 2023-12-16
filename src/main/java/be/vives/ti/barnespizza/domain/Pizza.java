package be.vives.ti.barnespizza.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
@Entity
@Table(name = "pizzas")
public class Pizza {

    public enum PizzaSize {
        SMALL,
        MEDIUM,
        LARGE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive value")
    private Double price;

    @Enumerated(EnumType.STRING)
    private PizzaSize size;

    @NotNull(message = "Price is required")
    private String description;

    @OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL)
    private List<PizzaOrderItem> pizzaOrderItems;

    protected Pizza(){

    }

    public Pizza(String name, Double price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
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

    public PizzaSize getSize() {
        return size;
    }

    public void setSize(PizzaSize size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PizzaOrderItem> getPizzaOrderItems() {
        return pizzaOrderItems;
    }

    public void setPizzaOrderItems(List<PizzaOrderItem> pizzaOrderItems) {
        this.pizzaOrderItems = pizzaOrderItems;
    }
}
