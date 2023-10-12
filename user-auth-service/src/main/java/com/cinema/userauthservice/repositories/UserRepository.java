package com.cinema.userauthservice.repositories;

import com.cinema.userauthservice.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Optional<User> findById(Long userId);

    @Query(value = "select u from User u where u.email like %:searchPattern% or u.name like %:searchPattern% or u.surname like %:searchPattern%")
    Page<User> filterUsers(Pageable pageable, @Param("searchPattern") String searchPattern);

    void removeUsersByEnabledIsFalse();

}
