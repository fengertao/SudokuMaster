/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class JwtTokenAndUserInfoResponse implements Serializable {

    private static final long serialVersionUID = 8317676219297719109L;

    private final String token;
    private final String username;
    private final String fullname;
    private final List<String> roles;

    JwtTokenAndUserInfoResponse(String token, String username, String fullname, Collection<? extends GrantedAuthority> roles) {
        this.token = token;
        this.username = username;
        this.fullname = fullname;
        this.roles = roles.stream().map((GrantedAuthority role) -> role.getAuthority()).collect(Collectors.toList());
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getToken() {
        return this.token;
    }
}