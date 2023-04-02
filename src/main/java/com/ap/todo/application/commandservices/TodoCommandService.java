package com.ap.todo.application.commandservices;

import com.ap.todo.application.outboundservices.ManagerOutboundService;
import com.ap.todo.application.queryservices.TodoQueryService;
import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.domain.repositories.TodoRepository;
import com.ap.todo.domain.valueobjects.Manager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoCommandService {

    private final TodoQueryService todoQueryService;
    private final ManagerOutboundService managerOutboundService;
    private final TodoRepository todoRepository;

    public Todo create(CreateTodoCommand command) {
        String managerId = command.getManagerId();
        // 해당 담당자의 to-do 조회
        List<Todo> todoList = todoQueryService.findTodoList(managerId);
        // 담당자 정보 조회
        Manager manager = managerOutboundService.findManagerInfo(managerId);
        // To-Do 기본값 설정 후 우선 순위 적용
        Todo todo = new Todo(command, manager);
        todo.applyPriority();

        todoRepository.save(todo);

        return todo;
    }

}
