package com.cinema.userauthservice.domain;

import com.cinema.userauthservice.domain.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @<code>User</code> - Entity that represents a user that is registered in this cinema managment app system.
 * Attributes:
 * <ul>
 *     <li><code>email</code> - Users/Employees email address that must be in gmail domain</li>
 *     <li><code>password</code> - Users/Employees password</li>
 *     <li><code>name</code> - Users/Employees first name</li>
 *     <li><code>surname</code> - Users/Employees last name</li>
 *     <li><code>role</code> - Users/Employees role that can be one of the following:
 *     <ul>
 *         <li><b>USER</b> - Ordinary user</li>
 *         <li><b>ADMIN</b> - Super user (This user cannot be made by calling API, it is "hardcoded" in the database</li>
 *         <li><b>CASHIER</b> - Employee type (Only made by an admin)</li>
 *         <li><b>MANAGER</b> - Employee type (Only made by an admin)</li>
 *     </ul>
 *     </li>
 *     <li><code>enabled</code> - Stores the info of wheter the ordinary user is verified or not,
 *     so if not, he can't log into the system</li>
 *     <li><code>tokens</code> - Collection of verification tokens for a record</li>
 * </ul>
 * @see org.springframework.security.core.userdetails.UserDetails
 * @implNote This <code>User</code> implements user details interface mentioned above to have fine grained control over
 * user authentication and authorization in this system.
 */


@SuppressWarnings("all")
@Entity
@Table(name = "user")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(unique = true,nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(length = 15,nullable = false)
    private String name;
    @Column(length = 15,nullable = false)
    private String surname;
    @Enumerated(EnumType.STRING)
    @Column(length = 12,nullable = false)
    private UserRole role;
    private boolean enabled = false;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<ConfirmationToken> tokens = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
