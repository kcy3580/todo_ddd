package com.ap.todo.interfaces.rest;

import com.ap.common.constants.ResultCode;
import com.ap.common.exception.ApiException;
import com.ap.todo.application.commandservices.TodoCommandService;
import com.ap.todo.application.outboundservices.ManagerOutboundService;
import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.domain.commands.UpdateTodoCommand;
import com.ap.todo.domain.queries.FindTodoQuery;
import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.interfaces.dto.*;
import com.ap.todo.interfaces.mapper.CreateTodoMapper;
import com.ap.todo.interfaces.mapper.FindManagerInfoListMapper;
import com.ap.todo.interfaces.mapper.FindTodoMapper;
import com.ap.todo.interfaces.mapper.UpdateTodoMapper;
import com.ap.utils.SessionHeaderMockMvcFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.spi.mapper.MappingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.ap.common.constants.StaticValues.RESULT_CODE;
import static com.ap.common.constants.StaticValues.RESULT_MESSAGE;
import static com.ap.todo.constant.TodoApiUrl.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    private FindTodoMapper findTodoMapper;

    @MockBean
    private FindManagerInfoListMapper findManagerInfoListMapper;

    @MockBean
    private UpdateTodoMapper updateTodoMapper;

    @Test
    @DisplayName("To-Do를 성공적으로 생성했다.")
    void createTodo_success() throws Exception {
        //given
        CreateTodoReqDto reqDto = CreateTodoReqDto.builder()
                .task("할일 1")
                .managerId("manager1")
                .executionDate("20230331123000")
                .description("이것은 할일 1 입니다.")
                .build();

        CreateTodoCommand command = CreateTodoCommand.builder()
                .task(reqDto.getTask())
                .managerId(reqDto.getManagerId())
                .executionDate(LocalDateTime.parse(reqDto.getExecutionDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss")))
                .description(reqDto.getDescription())
                .build();
        given(createTodoMapper.toCommand(any())).willReturn(command);

        Todo todo = Todo.builder()
                .todoId(1L)
                .task(command.getTask())
                .manager(new Manager(command.getManagerId(), "testerName"))
                .executionDate(command.getExecutionDate())
                .description(command.getDescription())
                .build();
        given(todoCommandService.create(any())).willReturn(todo);

        CreateTodoRspDto rspDto = CreateTodoRspDto.builder()
                .todoId(todo.getTodoId())
                .task(todo.getTask())
                .description(todo.getDescription())
                .build();
        given(createTodoMapper.toResponseDto(any())).willReturn(rspDto);

        //when, then
        mockMvc.perform(SessionHeaderMockMvcFactory.post(TODO_BASE_URL + CREATE_TODO_URL)
                .content(objectMapper.writeValueAsString(reqDto)))
                .andDo(print())
                .andExpect(jsonPath("$.todoId").value(rspDto.getTodoId()))
                .andExpect(jsonPath("$.task").value(rspDto.getTask()))
                .andExpect(jsonPath("$.description").value(rspDto.getDescription()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(header().stringValues(RESULT_CODE, ResultCode.SUCCESS.getCode()))
                .andExpect(header().stringValues(RESULT_MESSAGE, ResultCode.SUCCESS.getUrlEncodingMessage()));
    }

    @Test
    @DisplayName("담당자 ID와 실행날짜 기준으로 To-Do 목록조회에 성공했다.")
    void findTodo_success() throws Exception {
        //given
        FindTodoQuery query = FindTodoQuery.builder()
                .managerId("testerId")
                .executionDate(LocalDateTime.now())
                .build();
        given(findTodoMapper.toQuery(anyString(), anyString())).willReturn(query);

        List<Todo> todoList = new ArrayList<>();
        Todo todo = Todo.builder()
                .todoId(1L)
                .build();
        todoList.add(todo);
        given(todoCommandService.findByManagerIdAndExecutionDate(any())).willReturn(todoList);

        List<FindTodoRspDto> rspDtoList = new ArrayList<>();
        FindTodoRspDto rspDto = FindTodoRspDto.builder()
                .todoId(todo.getTodoId())
                .build();
        rspDtoList.add(rspDto);
        given(findTodoMapper.toResponseDtoList(anyList())).willReturn(rspDtoList);

        //when, then
        mockMvc.perform(SessionHeaderMockMvcFactory.get(TODO_BASE_URL + FIND_TODO_URL.concat("?executionDate=" + "20230331123000")))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(objectMapper.writeValueAsString(rspDtoList)))
                .andExpect(header().stringValues(RESULT_CODE, ResultCode.SUCCESS.getCode()))
                .andExpect(header().stringValues(RESULT_MESSAGE, ResultCode.SUCCESS.getUrlEncodingMessage()));
    }

    @Test
    @DisplayName("담당자 ID가 헤더에 존재하지 않아 오류가 발생했다.")
    void findTodo_no_managerid_fail() throws Exception {
        //given
        //when, then
        mockMvc.perform(SessionHeaderMockMvcFactory.getForNoManagerId(TODO_BASE_URL + FIND_TODO_URL.concat("?executionDate=" + "20230331123000")))
                .andDo(print())
                .andExpect(status().is4xxClientError());
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
    @DisplayName("To-Do를 업데이트한다.")
    void changeTodo() throws Exception {
        // given
        UpdateTodoReqDto reqDto = UpdateTodoReqDto.builder()
                .todoId("todoId")
                .build();
        UpdateTodoCommand command = UpdateTodoCommand.builder()
                .todoId(reqDto.getTodoId())
                .build();
        given(updateTodoMapper.toCommand(any())).willReturn(command);
        doNothing().when(todoCommandService).updateInfo(any());

        //when, then
        mockMvc.perform(SessionHeaderMockMvcFactory.patch(TODO_BASE_URL + UPDATE_TODO_URL)
                        .content(objectMapper.writeValueAsString(reqDto)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(header().stringValues(RESULT_CODE, ResultCode.SUCCESS.getCode()))
                .andExpect(header().stringValues(RESULT_MESSAGE, ResultCode.SUCCESS.getUrlEncodingMessage()));
    }

    @Test
    @DisplayName("To-Do를 삭제한다.")
    void deleteTodo() throws Exception {
        //given
        doNothing().when(todoCommandService).deleteByTodoId(anyString());
        //when, then
        mockMvc.perform(SessionHeaderMockMvcFactory.delete(TODO_BASE_URL + DELETE_TODO_URL, "1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(header().stringValues(RESULT_CODE, ResultCode.SUCCESS.getCode()))
                .andExpect(header().stringValues(RESULT_MESSAGE, ResultCode.SUCCESS.getUrlEncodingMessage()));
    }

}