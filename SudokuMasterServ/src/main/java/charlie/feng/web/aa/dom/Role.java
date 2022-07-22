/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.dom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Map;
import java.util.TreeMap;

@Entity
public class Role {

    public static final Role ROLE_ROOT = new Role("ROLE_ROOT", "Root");
    public static final Role ROLE_ADMIN = new Role("ROLE_ADMIN", "Admin");
    public static final Role ROLE_VIP = new Role("ROLE_VIP", "Vip User");
    public static final Role ROLE_USER = new Role("ROLE_USER", "User");
    public static final Role ROLE_APPLICANT = new Role("ROLE_APPLICANT", "Applicant");
    public static final Role ROLE_ANONYMOUS = new Role("ROLE_ANONYMOUS", "Guest");
    private static final Map<String, Role> ROLES_MAP = new TreeMap<>();

    static {
        ROLES_MAP.put(ROLE_ROOT.description, ROLE_ROOT);
        ROLES_MAP.put(ROLE_ADMIN.description, ROLE_ADMIN);
        ROLES_MAP.put(ROLE_VIP.description, ROLE_VIP);
        ROLES_MAP.put(ROLE_USER.description, ROLE_USER);
        ROLES_MAP.put(ROLE_APPLICANT.description, ROLE_APPLICANT);
        ROLES_MAP.put(ROLE_ANONYMOUS.description, ROLE_ANONYMOUS);
    }

    @Id
    @Column(nullable = false, length = 50)
    private String name;

    @Column
    private String description;

    public Role() {

    }

    private Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static Role valueOf(String desc) {
        return ROLES_MAP.get(desc);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
