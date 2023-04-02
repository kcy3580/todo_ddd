package com.ap.todo.interfaces.rest;

import com.ap.todo.application.commandservices.TodoCommandService;
import com.ap.todo.application.outboundservices.ManagerOutboundService;
import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.interfaces.dto.CreateTodoReqDto;
import com.ap.todo.interfaces.dto.CreateTodoRspDto;
import com.ap.todo.interfaces.dto.FindMangerInfoRspDto;
import com.ap.todo.interfaces.mapper.CreateTodoMapper;
import com.ap.todo.interfaces.mapper.FindManagerInfoListMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    private final FindManagerInfoListMapper findManagerInfoListMapper;

    /**
     * To-Do를 생성한다.
     * @param reqDto    생성요청 Dto
     * */
    @PostMapping(CREATE_TODO_URL)
    public ResponseEntity<Object> createTodo(@RequestBody CreateTodoReqDto reqDto) {
        CreateTodoCommand command = createTodoMapper.toCommand(reqDto);
        Todo todo = todoCommandService.create(command);
        CreateTodoRspDto rspDto = createTodoMapper.toResponseDto(todo);
        return new ResponseEntity<>(rspDto, getSuccessHeaders(), HttpStatus.OK);
    }

    @GetMapping(FIND_TODO_URL)
    public ResponseEntity<Object> findTodo() {

        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

    @GetMapping(FIND_MANAGER_INFO_LIST_URL)
    public ResponseEntity<List<FindMangerInfoRspDto>> findMangerInfoList() {
        List<Manager> managerInfoList = managerOutboundService.findManagerInfoList();
        List<FindMangerInfoRspDto> rspDtoList = findManagerInfoListMapper.toListResponseDto(managerInfoList);
        return new ResponseEntity<>(rspDtoList, getSuccessHeaders(), HttpStatus.OK);
    }

    @PatchMapping(CHANGE_TODO_URL)
    public ResponseEntity<Object> changeTodo() {

        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

    @DeleteMapping(DELETE_TODO_URL)
    public ResponseEntity<Object> deleteTodo() {

        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

}
