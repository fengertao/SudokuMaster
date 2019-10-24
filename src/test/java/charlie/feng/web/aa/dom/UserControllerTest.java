/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.dom;

import charlie.feng.charlie.feng.web.GlobalControllerExceptionHandler;
import charlie.feng.game.sudokumasterserv.SudokumasterservApplication;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {LiquibaseTestConfig.class, SudokumasterservApplication.class})
@ActiveProfiles("dev")
@WithMockUser(username = "charlie", roles = {"ROOT"})
//@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:dao/TestData.sql")
public class UserControllerTest {

    private MockMvc mvc;

    @Autowired
    private SpringLiquibase springLiquibase;

    @Autowired
    private UserController userController;

    @Before
    public void setUp() throws LiquibaseException {
        mvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new GlobalControllerExceptionHandler()).build();
        springLiquibase.afterPropertiesSet();
    }

    @Test
    public void signup() throws Exception {
        JSONObject signupJson = new JSONObject();
        signupJson.put("username", "mockuser");
        signupJson.put("password", "mockpwd");
        signupJson.put("fullname", "Mock User");
        signupJson.put("email", "mockuser@mock.com");
        String signupContent = signupJson.toString();

        mvc.perform(MockMvcRequestBuilders.post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(signupContent))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string("User mockuser signed up"));
    }

    @Test
    public void signupExistingUsername() throws Exception {
        JSONObject signupJson = new JSONObject();
        signupJson.put("username", "warren");
        signupJson.put("password", "mockpwd");
        signupJson.put("fullname", "Mock User");
        signupJson.put("email", "mockuser@mock.com");
        String signupContent = signupJson.toString();

        mvc.perform(MockMvcRequestBuilders.post("/user/signup")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(signupContent))
                .andExpect(status().isConflict())
        ;
    }

    @Test
    public void getAllUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(23))
                .andExpect(jsonPath("$[0].username").value("charlie"))
                .andExpect(jsonPath("$[0].password").doesNotExist())
                .andExpect(jsonPath("$[0].fullName").value("Charlie Feng"))
                .andExpect(jsonPath("$[0].email").value("fengertao@outlook.com"))
                .andExpect(jsonPath("$[0].roles.length()").value(1))
                .andExpect(jsonPath("$[0].roles[0].description").value("Root")) //Todo not inside json
                .andExpect(jsonPath("$[1].username").value("angela"))
                .andExpect(jsonPath("$[1].roles.length()").value(1))
                .andExpect(jsonPath("$[1].roles[0].description").value("Admin"))
                .andExpect(jsonPath("$[2].username").value("warren"))
                .andExpect(jsonPath("$[2].roles.length()").value(1))
                .andExpect(jsonPath("$[2].roles[0].description").value("User"));
    }

    @Test
    public void findByUserName() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/username/charlie"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$.username").value("charlie"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.fullName").value("Charlie Feng"))
                .andExpect(jsonPath("$.email").value("fengertao@outlook.com"))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.roles.length()").value(1))
                .andExpect(jsonPath("$.roles[0].name").value("ROLE_ROOT"))
                .andExpect(jsonPath("$.roles[0].description").value("Root"));
        mvc.perform(MockMvcRequestBuilders.get("/user/username/angela"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$.username").value("angela"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.fullName").value("Angela Yang"))
                .andExpect(jsonPath("$.email").value("xiyanyangeco@outlook.com"))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.roles.length()").value(1))
                .andExpect(jsonPath("$.roles[0].name").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.roles[0].description").value("Admin"));
        mvc.perform(MockMvcRequestBuilders.get("/user/username/warren"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$.username").value("warren"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.fullName").value("Warren Feng"))
                .andExpect(jsonPath("$.email").value("fengwarren@outlook.com"))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.roles.length()").value(1))
                .andExpect(jsonPath("$.roles[0].name").value("ROLE_USER"))
                .andExpect(jsonPath("$.roles[0].description").value("User"));
        mvc.perform(MockMvcRequestBuilders.get("/user/username/noneExistingUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "angela", roles = {"ADMIN"})
    public void findByUserNameViaAdmin() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/username/angela"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$.username").value("angela"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.fullName").value("Angela Yang"))
                .andExpect(jsonPath("$.email").value("xiyanyangeco@outlook.com"))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.roles.length()").value(1))
                .andExpect(jsonPath("$.roles[0].description").value("Admin"));

        mvc.perform(MockMvcRequestBuilders.get("/user/username/charlie"))
                .andExpect(status().isUnauthorized());

        mvc.perform(MockMvcRequestBuilders.get("/user/username/noneExistingUser"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "warren", roles = {"USER"})
    public void findByUserNameViaUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/username/warren"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(6))
                .andExpect(jsonPath("$.username").value("warren"))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.fullName").value("Warren Feng"))
                .andExpect(jsonPath("$.email").value("fengwarren@outlook.com"))
                .andExpect(jsonPath("$.enabled").value(true))
                .andExpect(jsonPath("$.roles.length()").value(1))
                .andExpect(jsonPath("$.roles[0].description").value("User"));

        mvc.perform(MockMvcRequestBuilders.get("/user/username/mockuser01"))
                .andExpect(status().isUnauthorized());

        mvc.perform(MockMvcRequestBuilders.get("/user/username/noneExistingUser"))
                .andExpect(status().isUnauthorized());
    }

    @Test
//    @Test(expected = NestedServletException.class)
    @WithMockUser(username = "warren", roles = {"USER"})
    public void getAllUsersWithoutPermission() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "charlie", roles = {"VIP"})
    public void addNewUserWithoutPermission() throws Exception {
        JSONObject signupJson = new JSONObject();
        signupJson.put("username", "mockuser");
        signupJson.put("password", "mockpwd");
        signupJson.put("fullname", "Mock User");
        signupJson.put("email", "mockuser@mock.com");
        JSONArray roles = new JSONArray();
        roles.put("ROLE_USER");
        signupJson.put("roles", roles);
        String signupContent = signupJson.toString();

        mvc.perform(MockMvcRequestBuilders.post("/user/add")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(signupContent))
                .andExpect(status().isUnauthorized());
    }
}