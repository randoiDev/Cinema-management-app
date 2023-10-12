package com.cinema.userauthservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;

/**
 * @<code>Employee</code> - Entity that represents an employee that works in a concrete cinema.
 * @see User
 * This class extends <code>User</code> class so it gains its attributes such as email,password,name...
 * It is extended with joined inheritance type which was used to separate ordinary users info from employees info in 2 tables in database.
 * Attributes:
 * <ul>
 *     <li><code>birthDate</code> - Employees birth date</li>
 *     <li><code>phone</code> - Employees phone number based on Serbia phone number format</li>
 *     <li><code>idCardNumber</code> - Employees id card number based on Serbia id card number length and format</li>
 *     <li><code>cinemaId</code> - Id of cinema where employee works that refers to a record in cinema table in other service</li>
 *     <li><code>manager</code> - Id of employees manager that referes to a record containing manager employee in employee table</li>
 * </ul>
 *     @implNote Manager employee doesn't have his own superior!
 */

@SuppressWarnings("all")
@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class Employee extends User{

    @Column(name = "birth_date",nullable = false)
    private Date birthDate;
    @Column(length = 15,nullable = false)
    private String phone;
    @Column(name = "id_card_number",length = 20,unique = true,nullable = false)
    private String idCardNumber;
    @Column(name = "cinema_id",nullable = false)
    private Long cinemaId;
    @ManyToOne
    private Employee manager;

}
