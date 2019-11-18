/*
 * Copyright (c) 2018-2019,  Charlie Feng. All Rights Reserved.
 */

package charlie.feng.game.sudokumasterserv.rest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GridControllerTest {
    @Autowired
    private GridController gridController;
    private MockMvc mvc;

    private Matcher<String> numberRangeMatcher = new BaseMatcher<String>() {
        public boolean matches(Object value) {
            int intValue = Integer.parseInt(value.toString());
            return intValue > 100 && intValue < 1000;
        }

        public void describeTo(Description mismatchDescription) {
            mismatchDescription.appendText("Value is a Integer between 100 and 1000.");
        }
    };

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(gridController).build();
    }

    @Test
    void testResolveGrid() throws Exception {


        mvc.perform(MockMvcRequestBuilders.get("/grid/000000018948007050000008020053702000009000000000901430090600000030500876060000000/resolve"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("resolved").value(true))
                .andExpect(jsonPath("answer").value("325496718948127653176358924653742189419835267782961435891673542234519876567284391"))
                .andExpect(jsonPath("resolution").exists())
                .andExpect(jsonPath("resolution").isArray())
                .andExpect(jsonPath("resolution.length()").value(numberRangeMatcher))
                .andExpect(jsonPath("resolution[0].length()").value(8))
                .andExpect(jsonPath("resolution[0].level").value(0))
                .andExpect(jsonPath("resolution[0].techniques").value(""))
                .andExpect(jsonPath("resolution[0].refCells").isArray())
                .andExpect(jsonPath("resolution[0].refCells.length()").value(0))
                .andExpect(jsonPath("resolution[0].preChangeCandidates").value(""))
                .andExpect(jsonPath("resolution[0].index").value(1))
                .andExpect(jsonPath("resolution[0].cell").isEmpty())
                .andExpect(jsonPath("resolution[0].message").value("开始解决数独"))
                .andExpect(jsonPath("resolution[0].position").value("|||||||1|8|9|4|8|||7||5|||||||8||2|||5|3|7||2||||||9||||||||||9||1|4|3|||9||6|||||||3||5|||8|7|6||6|||||||"))
                .andExpect(jsonPath("resolution[1].length()").value(8))
                .andExpect(jsonPath("resolution[1].level").value(3))
                .andExpect(jsonPath("resolution[1].techniques").value("同行单元格已有该值"))
                .andExpect(jsonPath("resolution[1].refCells").isArray())
                .andExpect(jsonPath("resolution[1].refCells.length()").value(1))
                .andExpect(jsonPath("resolution[1].preChangeCandidates").value(""))
                .andExpect(jsonPath("resolution[1].refCells[0]]").value("(1,8)"))
                .andExpect(jsonPath("resolution[1].index").value(2))
                .andExpect(jsonPath("resolution[1].cell").value("(1,1)"))
                .andExpect(jsonPath("resolution[1].message").value("消除 [1] 剩余 [23456789]."))
                .andExpect(jsonPath("resolution[1].position").value("23456789|||||||1|8|9|4|8|||7||5|||||||8||2|||5|3|7||2||||||9||||||||||9||1|4|3|||9||6|||||||3||5|||8|7|6||6|||||||"))
                .andExpect(jsonPath("resolution[-1].length()").value(8))
                .andExpect(jsonPath("resolution[-1].level").value(0))
                .andExpect(jsonPath("resolution[-1].techniques").value(""))
                .andExpect(jsonPath("resolution[-1].refCells").isArray())
                .andExpect(jsonPath("resolution[-1].refCells.length()").value(0))
                .andExpect(jsonPath("resolution[-1].preChangeCandidates").value(""))
                .andExpect(jsonPath("resolution[-1].index").value(numberRangeMatcher))
                .andExpect(jsonPath("resolution[-1].cell").isEmpty())
                .andExpect(jsonPath("resolution[-1].message").value("成功解决数独"))
                .andExpect(jsonPath("resolution[-1].position").value("3|2|5|4|9|6|7|1|8|9|4|8|1|2|7|6|5|3|1|7|6|3|5|8|9|2|4|6|5|3|7|4|2|1|8|9|4|1|9|8|3|5|2|6|7|7|8|2|9|6|1|4|3|5|8|9|1|6|7|3|5|4|2|2|3|4|5|1|9|8|7|6|5|6|7|2|8|4|3|9|1"));

        //Test grid length
        mvc.perform(MockMvcRequestBuilders.get("/grid/12345/resolve"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("resolved").value(false))
                .andExpect(jsonPath("msg").value("数独盘面由81位数字组成。空格请输入0。"));

        //Test non digital char
        mvc.perform(MockMvcRequestBuilders.get("/grid/12345678912345678912345678912345678912345678912345678912345678912345678912345678a/resolve"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("resolved").value(false))
                .andExpect(jsonPath("msg").value("数独盘面由81位数字组成。空格请输入0。"));

        //        mvc.perform(MockMvcRequestBuilders.post("/user/post")
        //                .param("id","3")
        //                .param("age","18"))
        //                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testResolvePosition() throws Exception {
        String baseUrl = "/position/000000018948007050000008020053702000009000000000901430090600000030500876060000000/";
        mvc.perform(MockMvcRequestBuilders.get(baseUrl + "|1234|3579|||6|7|1|8|9|4|8|1|2|7|6|5|3|1|7|6|3|5|8|9|2|4|6|5|3|7|4|2|1|8|9|4|1|9|8|3|5|2|6|7|7|8|2|9|6|1|4|3|5|8|9|1|6|7|3|5|4|2|2|3|4|5|1|9|8|7|6|5|6|7|2|8|4|3|9|1" + "/resolve"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("resolved").value(true))
                .andExpect(jsonPath("isValid").value(true))
                .andExpect(jsonPath("answer").value("325496718948127653176358924653742189419835267782961435891673542234519876567284391"))
                .andExpect(jsonPath("resolution").exists())
                .andExpect(jsonPath("resolution").isArray())
                .andExpect(jsonPath("resolution.length()").value(16))
                .andExpect(jsonPath("resolution[0].length()").value(8))
                .andExpect(jsonPath("resolution[0].level").isNumber())
                .andExpect(jsonPath("resolution[0].techniques").isString())
                .andExpect(jsonPath("resolution[0].refCells").isArray())
                .andExpect(jsonPath("resolution[0].refCells.length()").value(0))
                .andExpect(jsonPath("resolution[0].preChangeCandidates").value(""))
                .andExpect(jsonPath("resolution[0].index").value(1))
                .andExpect(jsonPath("resolution[0].cell").isEmpty())
                .andExpect(jsonPath("resolution[0].message").value("开始解决数独"))
                .andExpect(jsonPath("resolution[0].position").value("|1234|3579|||6|7|1|8|9|4|8|1|2|7|6|5|3|1|7|6|3|5|8|9|2|4|6|5|3|7|4|2|1|8|9|4|1|9|8|3|5|2|6|7|7|8|2|9|6|1|4|3|5|8|9|1|6|7|3|5|4|2|2|3|4|5|1|9|8|7|6|5|6|7|2|8|4|3|9|1"))
                .andExpect(jsonPath("resolution[1].length()").value(8))
                .andExpect(jsonPath("resolution[1].level").isNumber())
                .andExpect(jsonPath("resolution[1].techniques").isString())
                .andExpect(jsonPath("resolution[1].refCells").isArray())
                .andExpect(jsonPath("resolution[1].refCells.length()").isNumber())
                .andExpect(jsonPath("resolution[1].preChangeCandidates").value(""))
                .andExpect(jsonPath("resolution[1].index").value(2))
                .andExpect(jsonPath("resolution[1].cell").value("(1,1)"))
                .andExpect(jsonPath("resolution[1].message").value("填入 [3]."))
                .andExpect(jsonPath("resolution[1].position").value("3|1234|3579|||6|7|1|8|9|4|8|1|2|7|6|5|3|1|7|6|3|5|8|9|2|4|6|5|3|7|4|2|1|8|9|4|1|9|8|3|5|2|6|7|7|8|2|9|6|1|4|3|5|8|9|1|6|7|3|5|4|2|2|3|4|5|1|9|8|7|6|5|6|7|2|8|4|3|9|1"))
                .andExpect(jsonPath("resolution[-1].length()").value(8))
                .andExpect(jsonPath("resolution[-1].level").value(0))
                .andExpect(jsonPath("resolution[-1].techniques").value(""))
                .andExpect(jsonPath("resolution[-1].refCells").isArray())
                .andExpect(jsonPath("resolution[-1].refCells.length()").value(0))
                .andExpect(jsonPath("resolution[-1].preChangeCandidates").value(""))
                .andExpect(jsonPath("resolution[-1].index").value(16))
                .andExpect(jsonPath("resolution[-1].cell").isEmpty())
                .andExpect(jsonPath("resolution[-1].message").value("成功解决数独"))
                .andExpect(jsonPath("resolution[-1].position").value("3|2|5|4|9|6|7|1|8|9|4|8|1|2|7|6|5|3|1|7|6|3|5|8|9|2|4|6|5|3|7|4|2|1|8|9|4|1|9|8|3|5|2|6|7|7|8|2|9|6|1|4|3|5|8|9|1|6|7|3|5|4|2|2|3|4|5|1|9|8|7|6|5|6|7|2|8|4|3|9|1"));

        mvc.perform(MockMvcRequestBuilders.get(baseUrl + "|1234|3579|||12345|7|1|8|9|4|8|1|2|7|6|5|3|1|7|6|3|5|8|9|2|4|6|5|3|7|4|2|1|8|9|4|1|9|8|3|5|2|6|7|7|8|2|9|6|1|4|3|5|8|9|1|6|7|3|5|4|2|2|3|4|5|1|9|8|7|6|5|6|7|2|8|4|3|9|1" + "/resolve"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("isValid").value(false))
                .andExpect(jsonPath("msg").value("单元格 (1,6) 内输入为 12345, 但其正确解应该为 6。"));

    }

    @Test
    void testValidatePosition() throws Exception {
        String baseUrl = "/position/000000018948007050000008020053702000009000000000901430090600000030500876060000000/";
        mvc.perform(MockMvcRequestBuilders.get(baseUrl + "|1234|3579|||6|7|1|8|9|4|8|1|2|7|6|5|3|1|7|6|3|5|8|9|2|4|6|5|3|7|4|2|1|8|9|4|1|9|8|3|5|2|6|7|7|8|2|9|6|1|4|3|5|8|9|1|6|7|3|5|4|2|2|3|4|5|1|9|8|7|6|5|6|7|2|8|4|3|9|1" + "/validate"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("isValid").value(true))
                .andExpect(jsonPath("msg").isEmpty());

        mvc.perform(MockMvcRequestBuilders.get(baseUrl + "|1234|3579|||12345|7|1|8|9|4|8|1|2|7|6|5|3|1|7|6|3|5|8|9|2|4|6|5|3|7|4|2|1|8|9|4|1|9|8|3|5|2|6|7|7|8|2|9|6|1|4|3|5|8|9|1|6|7|3|5|4|2|2|3|4|5|1|9|8|7|6|5|6|7|2|8|4|3|9|1" + "/validate"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("isValid").value(false))
                .andExpect(jsonPath("msg").value("单元格 (1,6) 内输入为 12345, 但其正确解应该为 6。"));
    }

}
