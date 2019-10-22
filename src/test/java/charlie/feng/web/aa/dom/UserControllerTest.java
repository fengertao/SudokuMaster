/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.web.aa.dom;

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
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
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
                .andExpect(status().isOk());
    }


    //Todo fix, why no exception?
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
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("charlie"))
//                .andExpect(jsonPath("$[0].password").doesNotExist()) //Todo no password
                .andExpect(jsonPath("$[0].fullName").value("Charlie Feng"))
                .andExpect(jsonPath("$[0].email").value("fengertao@outlook.com"))
                .andExpect(jsonPath("$[0].roles.length()").value(1))
                .andExpect(jsonPath("$[0].roles[0].description").value("Root")) //Todo not inside json
                .andExpect(jsonPath("$[1].username").value("warren"))
                .andExpect(jsonPath("$[1].roles.length()").value(1))
                .andExpect(jsonPath("$[1].roles[0].description").value("User"));
    }

    @Test
    public void findByUserName() throws Exception {
        //Todo
    }

    @Test(expected = NestedServletException.class)
    @WithMockUser(username = "warren", roles = {"USER"})
    public void getAllUsersWithoutPermission() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/user/all"));
    }


    @Test(expected = NestedServletException.class)
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
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }
}