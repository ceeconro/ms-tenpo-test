package org.tenpo.test.mstenpotest.multiply;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MultiplyControllerSpec {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Given two numbers, then will return the result of multiply them")
    @WithMockUser(roles = "admin")
    public void multiplyNumbers() throws Exception {
        mockMvc.perform(
                post("/multiply")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MultiplyRequest(new BigDecimal(2), new BigDecimal(4))))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(8));
    }

    @Test
    @DisplayName("Given and anonymous user, then will rejected with unauthorized status code")
    @WithAnonymousUser
    public void unauthorizedUserConsumingMultiplyNumbers() throws Exception {
        mockMvc.perform(
                post("/multiply")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MultiplyRequest(new BigDecimal(2), new BigDecimal(4))))
        )
                .andExpect(status().isUnauthorized());
    }

}