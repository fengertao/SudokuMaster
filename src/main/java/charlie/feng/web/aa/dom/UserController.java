/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.dom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Todo no stack track after exception in method.
@RestController
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //Todo check current user role. only admin can call this method with role non-empty
    //Todo check current user role, only root can call this method with role ADMIN and ROOT
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/add",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addNewUser(@RequestBody UserCreateForm userForm) {
        User user = fillInUserForm(userForm);
        user.setEnabled(userForm.isEnabled());
        user.addRoles(userForm.getRoles());
        userRepository.save(user);
        return "User " + user.getUsername() + " registered";
    }

    @PostMapping(value = "/signup",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String signup(@RequestBody UserCreateForm userForm) {
        User user = fillInUserForm(userForm);
        user.setEnabled(false);
        user.addRole(Role.ROLE_APPLICANT);
        userRepository.save(user);
        return "User " + user.getUsername() + " registered";
    }

    private User fillInUserForm(@RequestBody UserCreateForm userForm) {
        User existingUser = userRepository.findByUsername(userForm.getUsername());
        if (existingUser != null) {
            throw new DuplicateUsernameException();
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
    public Iterable<User> getAllUsers() {
        //Todo the return should not contains password
        //Todo Pagination
        return userRepository.findAll();
    }

    //Todo ACL
    //Todo remove password
    @PreAuthorize("hasRole('USER')")
    @GetMapping(path = "/username/{username}")
    @ResponseBody
    public User findByUsername(@PathVariable String username) {
        //Todo the return should not contains password
        return userRepository.findByUsername(username);
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