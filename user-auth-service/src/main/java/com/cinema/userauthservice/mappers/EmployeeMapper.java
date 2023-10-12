package com.cinema.userauthservice.mappers;

import com.cinema.userauthservice.domain.Employee;
import com.cinema.userauthservice.domain.enums.UserRole;
import com.cinema.userauthservice.dto.CreateEmployeeDto;
import com.cinema.userauthservice.dto.EmployeeDto;
import com.cinema.userauthservice.dto.UpdateEmployeeDto;
import com.cinema.userauthservice.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@SuppressWarnings("all")
public class EmployeeMapper {

    private final EmployeeRepository employeeRepository;

    public Employee toEntity(CreateEmployeeDto createEmployeeDto, UserRole role, Employee manager) {
        Employee em = new Employee();
        em.setEmail(createEmployeeDto.getEmail());
        em.setName(createEmployeeDto.getName());
        em.setSurname(createEmployeeDto.getSurname());
        em.setBirthDate(new java.sql.Date(createEmployeeDto.getBirthDate()));
        em.setPhone(createEmployeeDto.getPhone());
        em.setIdCardNumber(createEmployeeDto.getIdCardNumber());
        em.setRole(role);
        em.setCinemaId(createEmployeeDto.getCinemaId());
        if(manager != null) {
            em.setManager(manager);
        } else {
            em.setManager(null);
        }
        return em;
    }

    public Employee toEntity(UpdateEmployeeDto employeeDto, Employee em) {
        em.setEmail(employeeDto.getEmail());
        em.setName(employeeDto.getName());
        em.setSurname(employeeDto.getSurname());
        em.setBirthDate(new java.sql.Date(employeeDto.getBirthDate()));
        em.setPhone(employeeDto.getPhone());
        em.setIdCardNumber(employeeDto.getIdCardNumber());
        em.setRole(UserRole.MANAGER);
        em.setCinemaId(employeeDto.getCinemaId());
        em.setManager(null);
        return em;
    }

    public EmployeeDto toDto(Employee employee) {
        EmployeeDto employeeDto = EmployeeDto
                .builder()
                .id(employee.getId())
                .email(employee.getEmail())
                .name(employee.getName())
                .surname(employee.getSurname())
                .birthDate(employee.getBirthDate().getTime())
                .cinemaId(employee.getCinemaId())
                .phone(employee.getPhone())
                .idCardNumber(employee.getIdCardNumber())
                .role(employee.getRole())
                .build();
        if(employee.getManager() != null) {
            employeeDto.setManagerId(employee.getManager().getId());
        }
        return employeeDto;
    }
}
