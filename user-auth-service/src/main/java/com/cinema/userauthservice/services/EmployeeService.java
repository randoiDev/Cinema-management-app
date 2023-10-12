package com.cinema.userauthservice.services;

import com.cinema.userauthservice.domain.enums.UserRole;
import com.cinema.userauthservice.dto.CreateEmployeeDto;
import com.cinema.userauthservice.dto.EmployeeDto;
import com.cinema.userauthservice.dto.Response;
import com.cinema.userauthservice.dto.UpdateEmployeeDto;

import java.util.Collection;

public interface EmployeeService {

    Response createCashier(CreateEmployeeDto e, String authorization);

    Response createManager(CreateEmployeeDto e, String authorization);

    Response updateManagerEmployeeInfo(UpdateEmployeeDto employeeDto);

    Response deleteEmployee(Long id);

    Response deleteEmployeesByCinema(Long id);

    Collection<EmployeeDto> filterEmployeesByEmail(Integer page, Integer size, String searchPattern);

    Collection<EmployeeDto> filterEmployeesByManager(Integer page, Integer size, Long id);

    Collection<EmployeeDto> filterEmployeesByIdCardNumber(Integer page, Integer size, String searchPattern);

    Collection<EmployeeDto> filterEmployeesByRole(Integer page, Integer size, UserRole role);

    Collection<EmployeeDto> filterEmployeesByCinema(Integer page, Integer size, Long id);

}
