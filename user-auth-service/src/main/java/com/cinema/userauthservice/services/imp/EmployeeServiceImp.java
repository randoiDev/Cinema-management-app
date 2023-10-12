package com.cinema.userauthservice.services.imp;

import com.cinema.userauthservice.domain.Employee;
import com.cinema.userauthservice.domain.enums.UserRole;
import com.cinema.userauthservice.dto.CreateEmployeeDto;
import com.cinema.userauthservice.dto.EmployeeDto;
import com.cinema.userauthservice.dto.Response;
import com.cinema.userauthservice.dto.UpdateEmployeeDto;
import com.cinema.userauthservice.exceptions.IllegalStateExceptionExtension;
import com.cinema.userauthservice.exceptions.RecordNotFoundException;
import com.cinema.userauthservice.mappers.EmployeeMapper;
import com.cinema.userauthservice.repositories.EmployeeRepository;
import com.cinema.userauthservice.services.EmployeeService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @<code>EmployeeServiceImp</code> - Class that has methods that are responsible for invoking business logic for a specific endpoint call.
 */

@SuppressWarnings("all")
@Service
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepository employeeRepository;
    private final RestTemplate restTemplate;
    @Value("${cinema-service.host}")
    private String cinemaHostname;

    /**
     * Method for creating cashier employee,first it checks if the provided manager id is not null,
     * then if manager found by provided id goes check if that employee is actually manager,if that
     * went well then employee mapper maps fields from request to employee entity.Password is created
     * concatenating first 2 digits from id card number of employee to "cashier" string.Then this cashier's profile is enabled and
     * persisted in employee table.Cashier's manager needs to be from the same cinema so that check is performed also.
     * Here we are also checking if the cinema with the specified id exists within another service.
     * @exception IllegalStateExceptionExtension
     * @exception RecordNotFoundException
     * @see EmployeeMapper
     * @see CreateEmployeeDto
     * @param e
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response createCashier(CreateEmployeeDto e, String authorization) {
        if (e.getManagerId() == null)
            throw new IllegalStateExceptionExtension("Cashier must have a manager assigned to him!");
        Employee manager = employeeRepository.getEmployeeById(e.getManagerId())
                .orElseThrow(() -> new RecordNotFoundException(Employee.class.getSimpleName(),e.getManagerId() + ""));
        if(!manager.getRole().equals(UserRole.MANAGER))
            throw new IllegalStateExceptionExtension("Employees manager can only be employee with role manager!");
        if(manager.getCinemaId() != e.getCinemaId())
            throw new IllegalStateExceptionExtension("Cannot assign a manager from one cinema to an employee from another one!");
        String jwt = authorization.split(" ")[1];
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        HttpEntity httpEntity = new HttpEntity(null, httpHeaders);
        String uri = "http://" + cinemaHostname + ":8081/api/cinema/cinema_id?id=" + e.getCinemaId();
        restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Response.class);
        Employee employee = employeeMapper.toEntity(e, UserRole.CASHIER, manager);
        employee.setPassword(passwordEncoder.encode("cashier" + employee.getIdCardNumber().charAt(0) + employee.getIdCardNumber().charAt(1)));
        employee.setEnabled(true);
        employeeRepository.save(employee);
        return new Response("Cashier employee with email:" + e.getEmail() + " created!");
    }

    /**
     * Method for creating manager employee.Password is created
     * concatenating first 2 digits from first 2 digits from id card number of employee to "manager" string.
     * Then the manager account is enabled and persisted in employee table.
     * Here we are also checking if the cinema with the specified id exists within another service.
     * @see CreateEmployeeDto
     * @see EmployeeMapper
     * @param e
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response createManager(CreateEmployeeDto e, String authorization) {
        String jwt = authorization.split(" ")[1];
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        HttpEntity httpEntity = new HttpEntity(null, httpHeaders);
        String uri = "http://" + cinemaHostname + ":8081/api/cinema/cinema_id?id=" + e.getCinemaId();
        restTemplate.exchange(uri, HttpMethod.GET, httpEntity, Response.class);
        Employee employee = employeeMapper.toEntity(e, UserRole.MANAGER, null);
        employee.setPassword(passwordEncoder.encode("manager" + employee.getIdCardNumber().charAt(0) + employee.getIdCardNumber().charAt(1)));
        employee.setEnabled(true);
        employeeRepository.save(employee);
        return new Response("Manager employee with email:" + e.getEmail() + " created!");
    }

    /**
     * this method is for updating manager employee info.
     * It checks if the provided id is correct by trying to find that manager entity from employee table,
     * then if everything goes through it updates info.
     * @implNote When a manager profile is created and that manager leaves his position for some reason
     * then the new manager is linked to that account which was previously linked to the employee that left.
     * I implemented it like this because the deletion of the manager would start the deletion of all employees
     * who are under that manager because of reference integrity that I set in <code>Employee</code> class and to
     * evade creation of new accounts for all those employees, the only thing that would be need to be done is
     * updating the info of one manager account, that saves a lot of time!
     * @exception RecordNotFoundException
     * @see EmployeeMapper
     * @see UpdateEmployeeDto
     * @param employeeDto
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response updateManagerEmployeeInfo(UpdateEmployeeDto employeeDto) {
        Employee manager = employeeRepository.getEmployeeById(employeeDto.getManagerId())
                .orElseThrow(() -> new RecordNotFoundException(Employee.class.getSimpleName(), employeeDto.getManagerId() + ""));
        if(!manager.getRole().equals(UserRole.MANAGER))
            throw new IllegalStateExceptionExtension("You cannot change the info of a non manager employee!");
        employeeRepository.save(employeeMapper.toEntity(employeeDto,manager));
        return new Response("Info updated for manager employee with id:" + employeeDto.getManagerId() + "!");
    }

    /**
     * This method is responsible for deleting managers and cashiers.
     * If the manager is being deleted, then all of his subordinates
     * are deleted also!
     * @exception RecordNotFoundException
     * @exception IllegalStateExceptionExtension
     * @param id
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response deleteEmployee(Long id) {
        Employee employee = employeeRepository.getEmployeeById(id)
                .orElseThrow(() -> new RecordNotFoundException(Employee.class.getSimpleName(), id + ""));
        if (employee.getRole() == UserRole.MANAGER) {
            Collection<Employee> collection = employeeRepository.filterEmployeesByManagerWithoutPaging(employee);
            employeeRepository.deleteAll(collection);
        }
        employeeRepository.delete(employee);
        return new Response("Employee with id:" + id + " deleted!");
    }

    /**
     * This method is responsible for deleting managers and cashiers by cinema id.
     * @param id
     * @return <code>Response</code> instance with wrapped response message.
     */
    @Override
    public Response deleteEmployeesByCinema(Long id) {
        employeeRepository.deleteAllByCinemaId(id);
        return new Response("Employees with cinema id:" + id + " are deleted!");
    }

    /**
     * This method retrieves list of employees which are filtered by specified search pattern page by page.
     * Search pattern is matched with employees email.
     * @see EmployeeRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param searchPattern
     * @return <code>Collection</code> of employees.
     */
    @Override
    public Collection<EmployeeDto> filterEmployeesByEmail(Integer page, Integer size, String searchPattern) {
        Iterable<Employee> collection = employeeRepository.filterEmployeesByEmail(PageRequest.of(page, size), searchPattern);
        Iterator<Employee> iterator = collection.iterator();
        List<EmployeeDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(employeeMapper.toDto(iterator.next()));
        }
        return newCollection;
    }

    /**
     * This method retrieves list of employees which are filtered by specified search pattern page by page.
     * Search pattern is matched with employees manager id.
     * @see EmployeeRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param id
     * @return <code>Collection</code> of employees.
     */
    @Override
    public Collection<EmployeeDto> filterEmployeesByManager(Integer page, Integer size, Long id) {
        Iterable<Employee> collection = employeeRepository.filterEmployeesByManager(PageRequest.of(page, size), id);
        Iterator<Employee> iterator = collection.iterator();
        List<EmployeeDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(employeeMapper.toDto(iterator.next()));
        }
        return newCollection;
    }

    /**
     * This method retrieves list of employees which are filtered by specified search pattern page by page.
     * Search pattern is matched with employees id card number.
     * @see EmployeeRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param searchPattern
     * @return <code>Collection</code> of employees.
     */
    @Override
    public Collection<EmployeeDto> filterEmployeesByIdCardNumber(Integer page, Integer size, String searchPattern) {
        Iterable<Employee> collection = employeeRepository.filterEmployeesByIdCardNumber(PageRequest.of(page, size), searchPattern);
        Iterator<Employee> iterator = collection.iterator();
        List<EmployeeDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(employeeMapper.toDto(iterator.next()));
        }
        return newCollection;
    }

    /**
     * This method retrieves list of employees which are filtered by specified search pattern page by page.
     * Search pattern is matched with employees role.
     * @see EmployeeRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param role
     * @return <code>Collection</code> of users.
     */
    @Override
    public Collection<EmployeeDto> filterEmployeesByRole(Integer page, Integer size, UserRole role) {
        Iterable<Employee> collection = employeeRepository.filterEmployeesByRole(PageRequest.of(page, size), role);
        Iterator<Employee> iterator = collection.iterator();
        List<EmployeeDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(employeeMapper.toDto(iterator.next()));
        }
        return newCollection;
    }

    /**
     * This method retrieves list of employees which are filtered by specified search pattern page by page.
     * Search pattern is matched with employees cinema.
     * @see EmployeeRepository where the query for this filtration is specified for more detailed info on searching.
     * @param page
     * @param size
     * @param id
     * @return <code>Collection</code> of users.
     */
    @Override
    public Collection<EmployeeDto> filterEmployeesByCinema(Integer page, Integer size, Long id) {
        Iterable<Employee> collection = employeeRepository.filterEmployeesByCinema(PageRequest.of(page, size), id);
        Iterator<Employee> iterator = collection.iterator();
        List<EmployeeDto> newCollection = new ArrayList<>();
        while (iterator.hasNext()) {
            newCollection.add(employeeMapper.toDto(iterator.next()));
        }
        return newCollection;
    }

}
