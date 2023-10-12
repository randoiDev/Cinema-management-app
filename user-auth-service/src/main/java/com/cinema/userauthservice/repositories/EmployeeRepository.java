package com.cinema.userauthservice.repositories;

import com.cinema.userauthservice.domain.Employee;
import com.cinema.userauthservice.domain.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    Optional<Employee> getEmployeeById(Long id);

    void deleteAllByCinemaId(Long id);

    @Query(value = "select em from Employee em where em.email like %:searchPattern%")
    Page<Employee> filterEmployeesByEmail(Pageable pageable, @Param("searchPattern") String searchPattern);

    @Query(value = "select em from Employee em where em.manager.id = :id")
    Page<Employee> filterEmployeesByManager(Pageable pageable, @Param("id") Long id);

    @Query(value = "select em from Employee em where em.role = :role")
    Page<Employee> filterEmployeesByRole(Pageable pageable, @Param("role") UserRole role);

    @Query(value = "select em from Employee em where em.cinemaId = :cinemaId")
    Page<Employee> filterEmployeesByCinema(Pageable pageable, @Param("cinemaId") Long cinemaId);

    @Query(value = "select em from Employee em where em.idCardNumber = :idCardNumber")
    Page<Employee> filterEmployeesByIdCardNumber(Pageable pageable, @Param("idCardNumber") String searchPattern);

    @Query(value = "select em from Employee em where em.manager = :manager")
    Collection<Employee> filterEmployeesByManagerWithoutPaging(@Param("manager") Employee manager);

}
