package charlie.feng.web.aa.jwt;

import charlie.feng.web.GlobalControllerExceptionHandler;
import charlie.feng.game.sudokumasterserv.SudokumasterservApplication;
import charlie.feng.web.aa.dom.LiquibaseTestConfig;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringLiquibase;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {LiquibaseTestConfig.class, SudokumasterservApplication.class})
@ActiveProfiles("dev")
@WithAnonymousUser
public class JwtAuthenticationRestControllerTest {

    private MockMvc mvc;

    @Autowired
    private SpringLiquibase springLiquibase;

    @Autowired
    private JwtAuthenticationRestController controller;

    @Before
    public void setUp() throws LiquibaseException {
        //Standalone MVC builder do not load properties files.
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalControllerExceptionHandler())
                .addPlaceholderValue("jwt.path.refresh", "//refresh")
                .addPlaceholderValue("jwt.path.authentication", "//authenticate")
                .addPlaceholderValue("jwt.url.ui1", "http://localhost:3006")
                .addPlaceholderValue("jwt.url.ui2", "http://localhost:3000")
                .addPlaceholderValue("jwt.url.ui3", "http://127.0.0.1:3006")
                .addPlaceholderValue("jwt.url.ui4", "http://127.0.0.1:3000")
                .build();
        springLiquibase.afterPropertiesSet();
    }

    @Test
    public void login() throws Exception {
        JSONObject loginJson = new JSONObject();
        loginJson.put("username", "angela");
        loginJson.put("password", "888");
        String loginContent = loginJson.toString();

        mvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(loginContent))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.username").value("angela"))
                .andExpect(jsonPath("$.fullname").value("Angela Yang"))
                .andExpect(jsonPath("$.roles.length()").value(1))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_ADMIN"));
    }


    @Test
    public void loginBadPassword() throws Exception {
        JSONObject loginJson = new JSONObject();
        loginJson.put("username", "angela");
        loginJson.put("password", "889");
        String loginContent = loginJson.toString();

        mvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(loginContent))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("INVALID_CREDENTIALS"));
    }

    @Test
    public void loginNoneExistingUser() throws Exception {
        JSONObject loginJson = new JSONObject();
        loginJson.put("username", "NoneExistingUser");
        loginJson.put("password", "anyPassword");
        String loginContent = loginJson.toString();

        mvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(loginContent))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("INVALID_CREDENTIALS"));
    }

    @Test
    public void loginDisabledUser() throws Exception {
        JSONObject loginJson = new JSONObject();
        loginJson.put("username", "mockuser01");
        loginJson.put("password", "anyPassword");
        String loginContent = loginJson.toString();

        mvc.perform(MockMvcRequestBuilders.post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(loginContent))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("USER_DISABLED"));
    }

}
