package com.ap.todo.interfaces.rest;

import com.ap.todo.application.commandservices.TodoCommandService;
import com.ap.todo.application.outboundservices.ManagerOutboundService;
import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.commands.UpdateTodoCommand;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.domain.queries.FindTodoQuery;
import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.interfaces.dto.*;
import com.ap.todo.interfaces.mapper.UpdateTodoMapper;
import com.ap.todo.interfaces.mapper.CreateTodoMapper;
import com.ap.todo.interfaces.mapper.FindManagerInfoListMapper;
import com.ap.todo.interfaces.mapper.FindTodoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

import static com.ap.common.constants.StaticValues.EMPTY_HEADER_PARAMETER_MANAGER_ID;
import static com.ap.common.constants.StaticValues.EMPTY_QUERY_STRING_EXECUTION_DATE;
import static com.ap.todo.constant.TodoApiUrl.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = TODO_BASE_URL)
@Validated
@RestController
public class TodoController extends BaseController{

    private final TodoCommandService todoCommandService;
    private final ManagerOutboundService managerOutboundService;
    private final CreateTodoMapper createTodoMapper;
    private final FindTodoMapper findTodoMapper;
    private final FindManagerInfoListMapper findManagerInfoListMapper;
    private final UpdateTodoMapper updateTodoMapper;

    /**
     * To-Do를 생성한다.
     * @param reqDto    생성요청 Dto
     * */
    @PostMapping(CREATE_TODO_URL)
    public ResponseEntity<Object> createTodo(@RequestBody @Valid CreateTodoReqDto reqDto) {
        CreateTodoCommand command = createTodoMapper.toCommand(reqDto);
        Todo todo = todoCommandService.create(command);
        CreateTodoRspDto rspDto = createTodoMapper.toResponseDto(todo);
        return new ResponseEntity<>(rspDto, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * To-Do를 담당자ID와 실행날짜 기준으로 조회한다.
     * @param managerId         담당자 ID
     * @param executionDate     실행날짜(yyyyMMddHHmmss)
     * */
    @GetMapping(FIND_TODO_URL)
    public ResponseEntity<List<FindTodoRspDto>> findTodo(@RequestHeader(name = "manager-id") @NotEmpty(message = EMPTY_HEADER_PARAMETER_MANAGER_ID) String managerId,
                                           @RequestParam(name = "executionDate") @NotEmpty(message = EMPTY_QUERY_STRING_EXECUTION_DATE) String executionDate) {
        FindTodoQuery query = findTodoMapper.toQuery(managerId, executionDate);
        List<Todo> todoList = todoCommandService.findByManagerIdAndExecutionDate(query);
        List<FindTodoRspDto> rspDto = findTodoMapper.toResponseDtoList(todoList);
        return new ResponseEntity<>(rspDto, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * 담당자 프로필 목록을 조회한다.
     * */
    @GetMapping(FIND_MANAGER_INFO_LIST_URL)
    public ResponseEntity<List<FindMangerInfoRspDto>> findMangerInfoList() {
        List<Manager> managerInfoList = managerOutboundService.findManagerInfoList();
        List<FindMangerInfoRspDto> rspDtoList = findManagerInfoListMapper.toListResponseDto(managerInfoList);
        return new ResponseEntity<>(rspDtoList, getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * To-Do 내용을 변경한다.
     * @param reqDto    변경요청 Dto
     * */
    @PatchMapping(UPDATE_TODO_URL)
    public ResponseEntity<Object> changeTodo(@RequestBody @Valid UpdateTodoReqDto reqDto) {
        UpdateTodoCommand command = updateTodoMapper.toCommand(reqDto);
        todoCommandService.updateInfo(command);
        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

    /**
     * To-Do를 삭제한다.
     * @param todoId    To-Do 식별 값
     * */
    @DeleteMapping(DELETE_TODO_URL)
    public ResponseEntity<Object> deleteTodo(@PathVariable(name = "todoId") String todoId) {
        todoCommandService.deleteByTodoId(todoId);
        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

}
