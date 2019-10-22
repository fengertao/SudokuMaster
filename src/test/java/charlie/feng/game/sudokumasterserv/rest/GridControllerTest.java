/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.rest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GridControllerTest {
    @Autowired
    private GridController gridController;
    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(gridController).build();
    }

    @Test
    public void testResolve() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/grid/000000018948007050000008020053702000009000000000901430090600000030500876060000000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("resolved").value(true))
                .andExpect(jsonPath("result").value("325496718948127653176358924653742189419835267782961435891673542234519876567284391"));

        //Test grid length
        mvc.perform(MockMvcRequestBuilders.get("/grid/12345"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("resolved").value(false))
                .andExpect(jsonPath("msg").value("Wrong grid"));

        //Test non digital char
        mvc.perform(MockMvcRequestBuilders.get("/grid/12345678912345678912345678912345678912345678912345678912345678912345678912345678a"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("resolved").value(false))
                .andExpect(jsonPath("msg").value("Wrong grid"));

        //        mvc.perform(MockMvcRequestBuilders.post("/user/post")
        //                .param("id","3")
        //                .param("name","nasheng.yun")
        //                .param("age","18"))
        //                .andExpect(MockMvcResultMatchers.status().isOk());
        //
        //        mvc.perform(MockMvcRequestBuilders.get("/user/list"))
        //                .andExpect(MockMvcResultMatchers.status().isOk())
        //                .andDo(MockMvcResultHandlers.print());// print result

    }
}
