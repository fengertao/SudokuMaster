/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.jwt;

import charlie.feng.web.aa.dom.User;
import charlie.feng.web.aa.dom.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JwtJPAUserDetailsService implements UserDetailsService {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final List<JwtUserDetails> IN_MEMORY_USER_LIST = new ArrayList<>();

    // Build-in user will save into datadabase by liquibase. keep below code for reference only.
    // Call org.springframework.security.crypto.bcrypt.BCrypt.hashpw(plain_password, BCrypt.gensalt()) to generate password.
    // inMemoryUserList.add(new JwtUserDetails(1L, "charlie", "$2a$10$R1AqQ0jWE.uWwwzl5fwgOOdmWq0.yXCEqc65p4600KHJli.VK.Pke", "ROLE_ADMIN")); //password 888

    private final UserRepository userRepository;

    public JwtJPAUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<JwtUserDetails> findInMemory = IN_MEMORY_USER_LIST.stream()
                .filter(user -> user.getUsername().equals(username)).findFirst();

        if (findInMemory.isPresent()) {
            return findInMemory.get();
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username));
        } else {
            return new JwtUserDetails(user.getUsername(), user.getPassword(), user.isEnabled(), user.getRoles(), user.getFullName());
        }
    }

}
