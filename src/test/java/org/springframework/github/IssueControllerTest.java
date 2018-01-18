package org.springframework.github;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
@AutoConfigureJsonTesters
public class IssueControllerTest {

    @MockBean IssuesService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void issuesShouldReturnAllIssues() throws Exception {
        Mockito.when(this.service.allIssues()).thenReturn(Arrays.asList(
                new IssueDto("first", "second"),
                new IssueDto("third", "fourth")
        ));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/issues"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("@.[0].userName").value("first"))
                .andExpect(MockMvcResultMatchers.jsonPath("@.[1].userName").value("third"))
                .andExpect(MockMvcResultMatchers.jsonPath("@.[0].repository").value("second"))
                .andExpect(MockMvcResultMatchers.jsonPath("@.[1].repository").value("fourth"))
                .andDo(MockMvcRestDocumentation.document("issues"));
    }

}
