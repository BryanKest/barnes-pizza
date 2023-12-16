package be.vives.ti.barnespizza.repository;

import be.vives.ti.barnespizza.domain.Beverage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BeverageRepository extends JpaRepository<Beverage, Integer>{

    Optional<Beverage> findByName(String name);

}
