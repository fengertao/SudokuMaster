/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.dom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository

public interface UserRepository extends PagingAndSortingRepository<User, Integer> {

    String POST_FILTER_ADMIN_CANNOT_SEE_ROOT = "hasRole('ROOT') || ((hasRole('ADMIN') && !(filterObject.roles.contains(T(charlie.feng.web.aa.dom.Role).ROLE_ROOT)))) ";

    @PostFilter(POST_FILTER_ADMIN_CANNOT_SEE_ROOT)
    Iterable<User> findAll();

    /*
     * If the login user is Root, he can query everybody
     * If the login user is Admin but not Root, he can query everybody but root
     * This method cannot apply PostFilter, because return type Pageable.
     * The @Query depends on org.springframework.security.spring-security-data dependency in pom.xml
     * and depends on create of {@link charlie.feng.web.SpringSecurityDataBean}
     *
     * Other SPEL query examples:
     * @Query("select o from #{#entityName} o where o.roles.size>0 ")
     * @Query("select o from #{#entityName} o where 1 = ?#{hasRole('ROOT') ? 1: 0}")
     * @Query("select o from #{#entityName} o where o.username like 'mock%'")
     * @Query("select o from #{#entityName} o where o.username like ?#{ principal?.username }")
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Query("select o from #{#entityName} o where 1 = ?#{hasRole('ROOT') ? 1: 0} or (not ('ROLE_ROOT' member o.roles))")
    Page<User> findAll(Pageable pageable);

    //This method cannot add PostFilter, because it will be called during login
    //So please add PostFilter in related controller/service method.
    User findByUsername(String username);

}
