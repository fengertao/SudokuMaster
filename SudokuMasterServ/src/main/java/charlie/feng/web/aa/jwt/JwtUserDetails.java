/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.jwt;

import charlie.feng.web.aa.dom.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JwtUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 5155720064139820502L;

    private final String username;
    private final String password;
    private final boolean enabled;
    private final Collection<? extends GrantedAuthority> authorities;
    //Fullname is not part of spring security protocol.
    //However, we add it here to save extra front-backend communication
    private final String fullname;

    public JwtUserDetails(String username, String password, boolean enabled, List<Role> roles, String fullname) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;

        List<SimpleGrantedAuthority> newAuthorities = new ArrayList<>();
        roles.forEach(role -> newAuthorities.add(new SimpleGrantedAuthority(role.getName())));

        this.authorities = newAuthorities;
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

}
