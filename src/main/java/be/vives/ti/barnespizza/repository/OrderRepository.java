package be.vives.ti.barnespizza.repository;

import be.vives.ti.barnespizza.domain.Account;
import be.vives.ti.barnespizza.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByAccountId(int accountId);
}
