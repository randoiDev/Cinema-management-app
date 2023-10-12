package com.cinema.userauthservice.controllers;

import com.cinema.userauthservice.domain.enums.UserRole;
import com.cinema.userauthservice.dto.*;
import com.cinema.userauthservice.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @<code>EmployeeController</code> - Spring rest controller for accessing endpoints for manipulating cinemas employee data.
 */

@SuppressWarnings("all")
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * @see com.cinema.userauthservice.domain.enums.UserRole Endpoint that requires <b>ADMIN</b> privilege to be accessed.
     * It is responsible for craeting <b>CASHIER</b> employee in the system.
     * @see CreateEmployeeDto
     * @param createEmployeeDto
     * @see Response
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/cashier")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> createCashierEmployee(@Valid @RequestBody CreateEmployeeDto createEmployeeDto,
                                                          @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(employeeService.createCashier(createEmployeeDto, authorization));
    }

    /**
     * @see com.cinema.userauthservice.domain.enums.UserRole Endpoint that requires <b>ADMIN</b> privilege to be accessed.
     * It is responsible for craeting <b>MANAGER</b> employee in the system.
     * @see CreateEmployeeDto
     * @param createEmployeeDto
     * @see Response
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/manager")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> createManagerEmployee(@Valid @RequestBody CreateEmployeeDto createEmployeeDto,
                                                          @RequestHeader("Authorization") String authorization) {
        return ResponseEntity.ok(employeeService.createManager(createEmployeeDto, authorization));
    }

    /**
     * @see com.cinema.userauthservice.domain.enums.UserRole Endpoint that requires <b>ADMIN</b> privilege to be accessed.
     * It is responsible for updating <b>MANAGER</b> employees info.
     * @see UpdateEmployeeDto
     * @param employeeDto
     * @see Response
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/manager")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> updateManagerEmployeeInfo(@Valid @RequestBody UpdateEmployeeDto employeeDto) {
        return ResponseEntity.ok(employeeService.updateManagerEmployeeInfo(employeeDto));
    }

    /**
     * @see com.cinema.userauthservice.domain.enums.UserRole Endpoint that requires <b>ADMIN</b> privilege to be accessed.
     * It is responsible for calling employee service to delete a employee with specified id.
     * @param id
     * @see Response
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulMessageResponse")
            }
    )
    public ResponseEntity<Response> deleteEmployee(@RequestParam("id") Long id) {
        return ResponseEntity.ok(employeeService.deleteEmployee(id));
    }

    /**
     * @see com.cinema.userauthservice.domain.enums.UserRole Endpoint that requires <b>ADMIN</b> privilege to be accessed.
     * It is responsible for calling employee service to delete a employee with specified cinema id.
     * @param id
     * @see Response
     * @return <code>Response</code> instance with wrapped string actual response message.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/cinema")
    @Operation(
            hidden = true
    )
    public ResponseEntity<Response> deleteEmployeesByCinema(@RequestParam("id") Long id) {
        return ResponseEntity.ok(employeeService.deleteEmployeesByCinema(id));
    }

    /**
     * Endpoint that requires <b>ADMIN</b> or <b>MANAGER </b>privilege to be accessed.
     * It is responsible for calling user service to retrieve a list of employees, the filtration is done
     * by matching provided search pattern with emails of employees.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param searchPattern
     * @see com.cinema.userauthservice.domain.EmployeeDto
     * @return <code>Collection</code> of employees that are registered in the system.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @GetMapping("/email")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionEmployeeResponse")
            }
    )
    public ResponseEntity<Iterable<EmployeeDto>> filterEmployeesByEmail(@RequestParam(defaultValue = "0") Integer page,
                                                                      @RequestParam(defaultValue = "2") Integer size,
                                                                      @RequestParam("email") String searchPattern) {
        return ResponseEntity.ok(employeeService.filterEmployeesByEmail(page,size,searchPattern));
    }

    /**
     * Endpoint that requires <b>ADMIN</b> or <b>MANAGER </b>privilege to be accessed.
     * It is responsible for calling user service to retrieve a list of employees, the filtration is done
     * by matching provided id with id of employees manager.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param id
     * @see com.cinema.userauthservice.domain.EmployeeDto
     * @return <code>Collection</code> of employees that are registered in the system.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @GetMapping("/manager")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionEmployeeResponse")
            }
    )
    public ResponseEntity<Iterable<EmployeeDto>> filterEmployeesByManager(@RequestParam(defaultValue = "0") Integer page,
                                                                        @RequestParam(defaultValue = "2") Integer size,
                                                                        @RequestParam("id") Long id) {
        return ResponseEntity.ok(employeeService.filterEmployeesByManager(page,size,id));
    }

    /**
     * Endpoint that requires <b>ADMIN</b> or <b>MANAGER </b>privilege to be accessed.
     * It is responsible for calling user service to retrieve a list of employees, the filtration is done
     * by matching provided search pattern with idCardNumbers of employees.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param searchPattern
     * @see com.cinema.userauthservice.domain.EmployeeDto
     * @return <code>Collection</code> of employees that are registered in the system.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @GetMapping("/idCardNumber")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionEmployeeResponse")
            }
    )
    public ResponseEntity<Iterable<EmployeeDto>> filterEmployeesByIdCardNumber(@RequestParam(defaultValue = "0") Integer page,
                                                                        @RequestParam(defaultValue = "2") Integer size,
                                                                        @RequestParam("idCardNumber") String searchPattern) {
        return ResponseEntity.ok(employeeService.filterEmployeesByIdCardNumber(page,size,searchPattern));
    }

    /**
     * Endpoint that requires <b>ADMIN</b> or <b>MANAGER </b>privilege to be accessed.
     * It is responsible for calling user service to retrieve a list of employees, the filtration is done
     * by matching provided id with id of cinema in which employee is working.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @param id
     * @see com.cinema.userauthservice.domain.EmployeeDto
     * @return <code>Collection</code> of employees that are registered in the system.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @GetMapping("/cinema")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionEmployeeResponse")
            }
    )
    public ResponseEntity<Iterable<EmployeeDto>> filterEmployeesByCinema(@RequestParam(defaultValue = "0") Integer page,
                                                                          @RequestParam(defaultValue = "2") Integer size,
                                                                          @RequestParam("cinemaId") Long id) {
        return ResponseEntity.ok(employeeService.filterEmployeesByCinema(page,size,id));
    }

    /**
     * Endpoint that requires <b>ADMIN</b> or <b>MANAGER </b>privilege to be accessed.
     * It is responsible for calling user service to retrieve a list of employees, the filtration is done
     * by matching provided role with the role of employees.
     * Page parameter is here referring to a part of the obtained list that is going to be sent back to the client,
     * and size parameter is referring to a page size.
     * @param page
     * @param size
     * @see UserRole
     * @param role
     * @see com.cinema.userauthservice.domain.EmployeeDto
     * @return <code>Collection</code> of employees that are registered in the system.
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER')")
    @GetMapping("/role")
    @Operation(
            responses = {
                    @ApiResponse(responseCode = "400", ref = "badResponse"),
                    @ApiResponse(responseCode = "404", ref = "badResponse"),
                    @ApiResponse(responseCode = "500", ref = "badResponse"),
                    @ApiResponse(responseCode = "200", ref = "successfulCollectionEmployeeResponse")
            }
    )
    public ResponseEntity<Iterable<EmployeeDto>> filterEmployeesByRole(@RequestParam(defaultValue = "0") Integer page,
                                                                               @RequestParam(defaultValue = "2") Integer size,
                                                                               @RequestParam("role") UserRole role) {
        return ResponseEntity.ok(employeeService.filterEmployeesByRole(page,size,role));
    }

}
