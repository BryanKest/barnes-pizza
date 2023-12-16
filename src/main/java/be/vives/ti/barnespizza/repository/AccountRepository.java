package be.vives.ti.barnespizza.repository;

import be.vives.ti.barnespizza.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    List<Account> findByFname(String fname);

    List<Account> findByName(String name);

}
