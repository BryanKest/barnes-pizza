package be.vives.ti.barnespizza.repository;

import be.vives.ti.barnespizza.domain.BeverageOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeverageOrderItemRepository extends JpaRepository<BeverageOrderItem, Integer> {
}
