package be.vives.ti.barnespizza.repository;

import be.vives.ti.barnespizza.domain.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PizzaRepository extends JpaRepository<Pizza, Integer>{

    Optional<Pizza> findByName (String name);

}
