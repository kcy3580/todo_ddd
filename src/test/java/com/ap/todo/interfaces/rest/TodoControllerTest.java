package com.ap.todo.interfaces.rest;

import com.ap.common.constants.ResultCode;
import com.ap.common.exception.ApiException;
import com.ap.todo.application.commandservices.TodoCommandService;
import com.ap.todo.application.outboundservices.ManagerOutboundService;
import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.interfaces.dto.FindMangerInfoRspDto;
import com.ap.todo.interfaces.mapper.CreateTodoMapper;
import com.ap.todo.interfaces.mapper.FindManagerInfoListMapper;
import com.ap.utils.SessionHeaderMockMvcFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.spi.mapper.MappingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.ap.common.constants.StaticValues.RESULT_CODE;
import static com.ap.common.constants.StaticValues.RESULT_MESSAGE;
import static com.ap.todo.constant.TodoApiUrl.FIND_MANAGER_INFO_LIST_URL;
import static com.ap.todo.constant.TodoApiUrl.TODO_BASE_URL;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoCommandService todoCommandService;

    @MockBean
    private ManagerOutboundService managerOutboundService;

    @MockBean
    private CreateTodoMapper createTodoMapper;

    @MockBean
    private FindManagerInfoListMapper findManagerInfoListMapper;

    @Test
    void createTodo() {
    }

    @Test
    void findTodo() {
    }

    @Test
    @DisplayName("담당자 프로필 목록을 성공적으로 조회했다.")
    void findMangerInfoList_success() throws Exception {
        //given
        List<Manager> managerInfoList = List.of(
                new Manager("id1", "manger1"),
                new Manager("id2", "manger2"),
                new Manager("id3", "manger3"),
                new Manager("id4", "manger4"),
                new Manager("id5", "manger5")
        );
        given(managerOutboundService.findManagerInfoList()).willReturn(managerInfoList);

        List<FindMangerInfoRspDto> findMangerInfoRspDtoList = new ArrayList<>();
        managerInfoList.forEach(manager -> findMangerInfoRspDtoList.add(new FindMangerInfoRspDto(manager.getId(), manager.getName())));
        given(findManagerInfoListMapper.toListResponseDto(anyList())).willReturn(findMangerInfoRspDtoList);

        //when, then
        mockMvc.perform(SessionHeaderMockMvcFactory.get(TODO_BASE_URL + FIND_MANAGER_INFO_LIST_URL))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(header().stringValues(RESULT_CODE, ResultCode.SUCCESS.getCode()))
                .andExpect(header().stringValues(RESULT_MESSAGE, ResultCode.SUCCESS.getUrlEncodingMessage()));
    }

    @Test
    @DisplayName("담당자 프로필 목록 조회 중 담당자 도메인에서 오류가 발생했다.")
    void findMangerInfoList_outbound_fail() throws Exception {
        //given
        given(managerOutboundService.findManagerInfoList()).willThrow(new ApiException(ResultCode.SERVER_ERROR));

        //when, then
        mockMvc.perform(SessionHeaderMockMvcFactory.get(TODO_BASE_URL + FIND_MANAGER_INFO_LIST_URL))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(header().stringValues(RESULT_CODE, ResultCode.SERVER_ERROR.getCode()))
                .andExpect(header().stringValues(RESULT_MESSAGE, ResultCode.SERVER_ERROR.getUrlEncodingMessage()));
    }

    @Test
    @DisplayName("담당자 프로필 목록 조회 후 응답값 맵핑 중 오류가 발생했다.")
    void findMangerInfoList_mapping_fail() throws Exception {
        //given
        List<Manager> managerInfoList = List.of(
                new Manager("id1", "manger1"),
                new Manager("id2", "manger2"),
                new Manager("id3", "manger3"),
                new Manager("id4", "manger4"),
                new Manager("id5", "manger5")
        );
        given(managerOutboundService.findManagerInfoList()).willReturn(managerInfoList);
        given(findManagerInfoListMapper.toListResponseDto(anyList())).willThrow(new MappingException("응답값 맵핑 실패"));

        //when, then
        mockMvc.perform(SessionHeaderMockMvcFactory.get(TODO_BASE_URL + FIND_MANAGER_INFO_LIST_URL))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(header().stringValues(RESULT_CODE, ResultCode.SERVER_ERROR.getCode()))
                .andExpect(header().stringValues(RESULT_MESSAGE, ResultCode.SERVER_ERROR.getUrlEncodingMessage()));
    }

    @Test
    void changeTodo() {
    }

    @Test
    void deleteTodo() {
    }
}