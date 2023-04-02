package com.ap.todo.interfaces.rest;

import com.ap.todo.application.commandservices.TodoCommandService;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.interfaces.dto.CreateTodoReqDto;
import com.ap.todo.interfaces.mapper.CreateTodoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ap.todo.constant.TodoApiUrl.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = TODO_BASE_URL)
@Validated
@RestController
public class TodoController extends BaseController{

    private final TodoCommandService todoCommandService;
    private final CreateTodoMapper createTodoMapper;

    /**
     * To-Do를 생성한다.
     * @param reqDto    생성요청 Dto
     * */
    @PostMapping(CREATE_TODO_URL)
    public ResponseEntity<Object> createTodo(CreateTodoReqDto reqDto) {
        CreateTodoCommand command = createTodoMapper.toCommand(reqDto);
        todoCommandService.create(command);
        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

    @GetMapping(FIND_TODO_URL)
    public ResponseEntity<Object> findTodo() {

        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
    }

    @GetMapping(FIND_TODO_LIST_URL)
    public ResponseEntity<Object> findTodoList() {

        return new ResponseEntity<>(getSuccessHeaders(), HttpStatus.OK);
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
