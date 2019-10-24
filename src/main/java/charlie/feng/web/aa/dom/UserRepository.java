/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.dom;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository

public interface UserRepository extends CrudRepository<User, Integer> {

    static final String POST_FILTER_ADMIN_CANNOT_SEE_ROOT = "hasRole('ROOT') || ((hasRole('ADMIN') && !(filterObject.roles.contains(T(charlie.feng.web.aa.dom.Role).ROLE_ROOT)))) ";

    @PostFilter(POST_FILTER_ADMIN_CANNOT_SEE_ROOT)
    Iterable<User> findAll();

    //This method cannot add PostFilter, because it will be called during login
    //So please add PostFilter in related controller/service method.
    User findByUsername(String username);

}
