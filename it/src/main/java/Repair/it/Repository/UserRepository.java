package Repair.it.Repository;

import Repair.it.Entity.User;
import Repair.it.Enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    Page<User> findByrole(Role role, Pageable pageable);
    long countByRole(Role role);
}
