package uz.attendance_system.attendance_system.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uz.attendance_system.attendance_system.domain.User;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
   boolean existsByLogin(String login);

   User findByLogin(String login);
    
}
