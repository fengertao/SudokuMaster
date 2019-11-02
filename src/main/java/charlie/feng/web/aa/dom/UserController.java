/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.dom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String PRE_AUTHORIZE_USER_CAN_QUERY_SELF_ADMIN_CAN_QUERY_ALL = "(hasRole('USER') && #username == authentication.name) || hasRole('ADMIN')";
    private static final String POST_AUTHORIZE_ADMIN_CANNOT_SEE_ROOT = "hasRole('ROOT') ||  ((hasRole('ADMIN') && (returnObject.body== null || !(returnObject.body.roles.contains(T(charlie.feng.web.aa.dom.Role).ROLE_ROOT))))) || returnObject.body.username.equals(authentication.name)";

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PreAuthorize("(hasRole('ADMIN') && !(#userForm.roles.contains('ROLE_ROOT')) && !(#userForm.roles.contains('ROLE_ADMIN')))  || (hasRole('ROOT'))")
    @PostMapping(value = "/add",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> addNewUser(@RequestBody UserCreateForm userForm) {
        User user = fillInUserForm(userForm);
        user.setEnabled(userForm.isEnabled());
        user.addRoles(userForm.getRoles());
        userRepository.save(user);
        return new ResponseEntity<>("User " + user.getUsername() + " added.", HttpStatus.OK);
    }

    //Todo remove password
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/enable", "/disable"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<User> enableDisable(@RequestBody EnableDisableForm form, HttpServletRequest request) {
        String username = form.getUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Collection<? extends GrantedAuthority> grantedAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        //Only Root user can enable/disable Root and Admin user.
        if ((user.getRoles().contains(Role.ROLE_ROOT) || user.getRoles().contains(Role.ROLE_ADMIN)) && (!grantedAuthorities.contains(new SimpleGrantedAuthority(Role.ROLE_ROOT.getName())))) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }

        boolean isEnable = request.getRequestURI().endsWith("enable");
        if (isEnable) {
            if (user.isEnabled()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            user.setEnabled(true);
            if (user.getRoles().contains(Role.ROLE_APPLICANT)) {
                List<Role> newRoles = new ArrayList<>();
                newRoles.add(Role.ROLE_USER);
                user.setRoles(newRoles);
            }
            userRepository.save(user);
        } else {
            if (!user.isEnabled()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            user.setEnabled(false);

        }
        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping(value = "/signup",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> signup(@RequestBody UserCreateForm userForm) {
        User user = fillInUserForm(userForm);
        user.setEnabled(false);
        user.addRole(Role.ROLE_APPLICANT);
        userRepository.save(user);
        return new ResponseEntity<>("User " + user.getUsername() + " signed up", HttpStatus.OK);
    }

    private User fillInUserForm(@RequestBody UserCreateForm userForm) {
        User existingUser = userRepository.findByUsername(userForm.getUsername());
        if (existingUser != null) {
            throw new DuplicateUsernameException("USERNAME_IS_USED");
        }

        User user = new User();
        user.setUsername(userForm.getUsername());
        //Todo decouple Encrypt algorithm
        user.setPassword(BCrypt.hashpw(userForm.getPassword(), BCrypt.gensalt()));
        user.setFullName(userForm.getFullname());
        user.setEmail(userForm.getEmail());
        return user;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/all")
    @ResponseBody
    public ResponseEntity<Iterable<User>> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/page")
    @ResponseBody
    public ResponseEntity<Iterable<User>> getPageUsers(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        //Todo support dynamic sort
        //Todo support filter
        Pageable pageable = PageRequest.of(page, 10, Sort.by("username"));
        return new ResponseEntity<>(userRepository.findAll(pageable), HttpStatus.OK);
    }

    @PreAuthorize(PRE_AUTHORIZE_USER_CAN_QUERY_SELF_ADMIN_CAN_QUERY_ALL)
    @PostAuthorize(POST_AUTHORIZE_ADMIN_CANNOT_SEE_ROOT)
    @GetMapping(path = "/username/{username}")
    @ResponseBody
    public ResponseEntity<User> findByUsername(@PathVariable String username) {
        User user = userRepository.findByUsername(username);
        return new ResponseEntity<>(user, user == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);

    }

}

class UserCreateForm {
    private String username;
    private String password;
    private String fullname;
    private String email;
    private boolean enabled;
    private List<String> roles;

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

class EnableDisableForm {
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}