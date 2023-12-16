package be.vives.ti.barnespizza.requests;

import jakarta.validation.constraints.NotNull;

public class BeverageOrderItemRequest {

    @NotNull(message = "Beverage id must not be empty")
    private Integer beverageId;

    @NotNull(message = "Amount must not be empty")
    private Integer amount;

    public Integer getBeverageId() {
        return beverageId;
    }

    public void setBeverageId(Integer beverageId) {
        this.beverageId = beverageId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
