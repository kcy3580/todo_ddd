package com.ap.todo.application.commandservices;

import com.ap.common.constants.ResultCode;
import com.ap.common.exception.ApiException;
import com.ap.todo.application.outboundservices.ManagerOutboundService;
import com.ap.todo.application.queryservices.TodoQueryService;
import com.ap.todo.constant.Importance;
import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.domain.queries.FindTodoQuery;
import com.ap.todo.domain.repositories.ManagerRepository;
import com.ap.todo.domain.repositories.TodoRepository;
import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.domain.valueobjects.Priority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ap.common.constants.StaticValues.RESULT_CODE;
import static com.ap.common.constants.StaticValues.RESULT_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class TodoCommandServiceTest {

    @InjectMocks
    private TodoCommandService todoCommandService;

    @Mock
    private TodoQueryService todoQueryService;

    @Mock
    private ManagerOutboundService managerOutboundService;

    @Mock
    private TodoRepository todoRepository;

    @Test
    @DisplayName("To-Do를 생성한다: 기존에 아무 목록도 존재하지 않아 우선순위가 BO로 생성된다.")
    void create_first_todo_success() {
        // given
        given(todoQueryService.findTodoListByManagerId(anyString())).willReturn(new ArrayList<>());

        Manager manager = new Manager("testerId", "tester");
        given(managerOutboundService.findManagerInfo(anyString())).willReturn(manager);

        doNothing().when(todoRepository).save(any());

        // when
        CreateTodoCommand command = CreateTodoCommand.builder()
                .task("test content")
                .executionDate(LocalDateTime.now())
                .managerId(manager.getId())
                .build();
        Todo actual = todoCommandService.create(command);

        // then
        assertThat(actual.getPriority().getImportance()).isEqualTo(Importance.MIDDLE);
        assertThat(actual.getPriority().getSequence()).isEqualTo(0);
    }

    @Test
    @DisplayName("To-Do를 생성한다: 기존에 목록이 존재하여 가장 최하위 순위 다음 순번으로 생성된다.")
    void create_not_first_todo_success() {
        // given
        List<Todo> todoList = new ArrayList<>();
        Todo todo = Todo.builder()
                .todoId(1L)
                .executionDate(LocalDateTime.now())
                .priority(Priority.builder()
                        .importance(Importance.MIDDLE)
                        .sequence(0)
                        .build())
                .build();
        todoList.add(todo);
        given(todoQueryService.findTodoListByManagerId(anyString())).willReturn(todoList);

        Manager manager = new Manager("testerId", "tester");
        given(managerOutboundService.findManagerInfo(anyString())).willReturn(manager);

        doNothing().when(todoRepository).save(any());

        // when
        CreateTodoCommand command = CreateTodoCommand.builder()
                .task("test content")
                .executionDate(LocalDateTime.now())
                .managerId(manager.getId())
                .build();
        Todo actual = todoCommandService.create(command);

        // then
        assertThat(actual.getPriority().getImportance()).isEqualTo(todo.getPriority().getImportance());
        assertThat(actual.getPriority().getSequence()).isEqualTo(todo.getPriority().getSequence() + 1);
    }

    @Test
    @DisplayName("To-Do를 생성하는 중에 담당자 정보를 조회하다 오류가 발생했다.")
    void create_todo_fail_find_manager() {
        // given
        List<Todo> todoList = new ArrayList<>();
        Todo todo = Todo.builder()
                .todoId(1L)
                .executionDate(LocalDateTime.now())
                .priority(Priority.builder()
                        .importance(Importance.MIDDLE)
                        .sequence(0)
                        .build())
                .build();
        todoList.add(todo);
        given(todoQueryService.findTodoListByManagerId(anyString())).willReturn(todoList);
        given(managerOutboundService.findManagerInfo(anyString())).willThrow(new ApiException(ResultCode.NO_DATA));

        // when
        CreateTodoCommand command = CreateTodoCommand.builder()
                .task("test content")
                .executionDate(LocalDateTime.now())
                .managerId("testId")
                .build();
        Throwable thrown = catchThrowable(() -> todoCommandService.create(command));

        //Then
        assertThat(thrown)
                .isInstanceOf(ApiException.class)
                .hasFieldOrProperty(RESULT_CODE)
                .hasFieldOrProperty(RESULT_MESSAGE);

        assertThat(((ApiException)thrown).getResultCode())
                .isEqualTo(ResultCode.NO_DATA.getCode());
        assertThat(((ApiException)thrown).getResultMessage())
                .isEqualTo(ResultCode.NO_DATA.getMessage());
    }

    @Test
    @DisplayName("To-Do를 조회한다.")
    void findByManagerIdAndExecutionDate_success() {
        // given
        List<Todo> todoList = new ArrayList<>();
        Todo todo = Todo.builder()
                .todoId(1L)
                .executionDate(LocalDateTime.now())
                .priority(Priority.builder()
                        .importance(Importance.MIDDLE)
                        .sequence(0)
                        .build())
                .build();
        todoList.add(todo);
        given(todoQueryService.findTodoListByManagerIdAndExecutionDate(anyString(), any())).willReturn(todoList);

        // when
        FindTodoQuery query = FindTodoQuery.builder()
                .managerId("testId")
                .executionDate(LocalDateTime.now())
                .build();
        List<Todo> actual = todoCommandService.findByManagerIdAndExecutionDate(query);

        // then
        assertThat(actual.size()).isEqualTo(todoList.size());
    }

    @Test
    @DisplayName("To-Do를 조회 중 오류가 발생했다.")
    void findByManagerIdAndExecutionDate_fail() {
        // given
        given(todoQueryService.findTodoListByManagerIdAndExecutionDate(anyString(), any())).willThrow(new ApiException(ResultCode.SERVER_ERROR));

        // when
        FindTodoQuery query = FindTodoQuery.builder()
                .managerId("testId")
                .executionDate(LocalDateTime.now())
                .build();
        Throwable thrown = catchThrowable(() -> todoCommandService.findByManagerIdAndExecutionDate(query));

        //Then
        assertThat(thrown)
                .isInstanceOf(Exception.class)
                .hasFieldOrProperty(RESULT_CODE)
                .hasFieldOrProperty(RESULT_MESSAGE);

        assertThat(((ApiException)thrown).getResultCode())
                .isEqualTo(ResultCode.SERVER_ERROR.getCode());
        assertThat(((ApiException)thrown).getResultMessage())
                .isEqualTo(ResultCode.SERVER_ERROR.getMessage());
    }

}