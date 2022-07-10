/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.dom;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity (name = "appuser")
public class User {

    @Id
    @Column(nullable = false, length = 50)
    @Size(min = 6)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    private boolean enabled;

    @Column(length = 100)
    private String fullName;

    @Column(length = 100)
    @Email(message = "{errors.invalid_email}")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "authorities",
            joinColumns = {
                    @JoinColumn(name = "username")}, inverseJoinColumns = {
            @JoinColumn(name = "authority")},
            uniqueConstraints = @UniqueConstraint(columnNames = {"username", "authority"}))
    private List<Role> roles;

    public User() {
    }

    public User(String username, String password, boolean enabled, String fullName, String email, List<Role> roles) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.fullName = fullName;
        this.email = email;
        this.roles = new ArrayList<>();
        this.roles.addAll(roles);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = new ArrayList<>();
        this.roles.addAll(roles);
    }

    public void addRoles(List<String> newRoles) {
        this.roles = new ArrayList<>();
        for (String s : newRoles) {
            this.roles.add(Role.valueOf(s));
        }
    }

    public void addRole(Role authority) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }
        this.roles.add(authority);
    }
}
