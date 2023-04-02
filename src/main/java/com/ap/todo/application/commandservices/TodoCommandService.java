package com.ap.todo.application.commandservices;

import com.ap.todo.application.outboundservices.ManagerOutboundService;
import com.ap.todo.application.queryservices.TodoQueryService;
import com.ap.todo.constant.Importance;
import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.domain.repositories.TodoRepository;
import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.domain.valueobjects.Priority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        Todo todo = new Todo((long) todoList.size() + 1, command, manager, calculatePriority(todoList, command.getExecutionDate()));

        todoRepository.save(todo);

        return todo;
    }

    private Priority calculatePriority(List<Todo> todoList, LocalDateTime executionDate) {
        Priority priority;
        if(todoList.size() == 0 || todoList.stream().noneMatch(todo -> todo.isSameExecutionDate(executionDate))) {
            priority = new Priority(Importance.MIDDLE, 0);
        } else {
            List<Todo> sameDateTodoList = todoList.stream().filter(todo -> todo.isSameExecutionDate(executionDate)).sorted(Comparator.comparing(Todo::getPriority)).collect(Collectors.toList());

            Todo lowestTodo = sameDateTodoList.get(0);

            priority = new Priority(lowestTodo.getPriority().getImportance(), lowestTodo.getPriority().getSequence() + 1);
        }
        return priority;
    }

}
