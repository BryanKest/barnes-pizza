package be.vives.ti.barnespizza.repository;

import be.vives.ti.barnespizza.domain.PizzaOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaOrderItemRepository extends JpaRepository<PizzaOrderItem, Integer> {
}
