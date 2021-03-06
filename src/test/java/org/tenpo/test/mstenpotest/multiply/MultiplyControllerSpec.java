package org.tenpo.test.mstenpotest.multiply;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class MultiplyControllerSpec {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MultiplyRequest validRequest;

    @MockBean
    private MultiplyService multiplyService;

    @BeforeEach
    private void setup() {
        validRequest = new MultiplyRequest(new BigDecimal(2), new BigDecimal(4));
        ResultResponse resultResponse = new ResultResponse(new BigDecimal(8));
        when(multiplyService.multiply(validRequest))
                .thenReturn(resultResponse);
    }


    @Test
    @DisplayName("Given two numbers, then will return the result of multiply them")
    @WithMockUser(roles = "admin")
    public void multiplyNumbers() throws Exception {
        ResultMatcher statusMatcher = status().isOk();
        MultiplyRequest validRequest = this.validRequest;
        performRequest(statusMatcher, validRequest)
                .andExpect(jsonPath("$.result").value(8));
    }

    @Test
    @DisplayName("Given and anonymous user, then will rejected with unauthorized status code")
    @WithAnonymousUser
    public void unauthorizedUserConsumingMultiplyNumbers() throws Exception {
        performRequest(status().isUnauthorized(), validRequest);
    }

    @Test
    @DisplayName("Given empty numbers, then will thrown validation exception")
    @WithMockUser(roles = "admin")
    public void validateEmptyNumbers() throws Exception {
        performRequest(status().isBadRequest(), new MultiplyRequest());

    }

    @Test
    @DisplayName("Given a valid multiply request, thwn will persist the operation")
    @WithMockUser(roles = "admin")
    public void persistValidMultiply() throws Exception {

        performRequest(status().isOk(), validRequest)
                .andExpect(jsonPath("$.result").value(8));

        verify(multiplyService, times(1)).multiply(validRequest);
    }

    @Test
    @DisplayName("Given a request of history data, then will return a pageable data list")
    @WithMockUser(roles = "admin")
    void getMultiplyHistoryListPaginated() throws Exception {
        when(multiplyService.getMultiplyHistoryPages(PageRequest.of(0, 3)))
                .thenReturn(getMultiplyResponseList());

        mockMvc.perform(
                get("/multiply/history?page=0&size=3")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numberOfElements").value(3));

    }

    private PageImpl getMultiplyResponseList() {
        return new PageImpl(Arrays.asList(new MultiplyResponse(), new MultiplyResponse(), new MultiplyResponse()));
    }

    private ResultActions performRequest(ResultMatcher statusMatcher, MultiplyRequest validRequest) throws Exception {
        return mockMvc.perform(
                post("/multiply")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest))
        )
                .andDo(print())
                .andExpect(statusMatcher);
    }
}
