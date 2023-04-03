package com.ap.todo.application.commandservices;

import com.ap.todo.application.outboundservices.ManagerOutboundService;
import com.ap.todo.application.queryservices.TodoQueryService;
import com.ap.todo.constant.Importance;
import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.domain.queries.FindTodoQuery;
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

    /**
     * To-Do를 생성한다.
     * @param command    생성요청 command
     * */
    public Todo create(CreateTodoCommand command) {
        try {
            String managerId = command.getManagerId();
            // 해당 담당자의 to-do 조회
            List<Todo> todoList = todoQueryService.findTodoListByManagerId(managerId);
            // 담당자 정보 조회
            Manager manager = managerOutboundService.findManagerInfo(managerId);
            // To-Do 기본값 설정 후 우선 순위 적용
            Todo todo = new Todo((long) todoList.size() + 1, command, manager, calculatePriority(todoList, command.getExecutionDate()));

            todoRepository.save(todo);

            return todo;
        } catch(Exception e) {
            log.error("To-Do 생성 중 오류발생", e);
            throw e;
        }
    }

    /**
     * To-Do를 담당자ID와 실행날짜 기준으로 조회한다.
     * @param query     조회 query
     * */
    public List<Todo> findByManagerIdAndExecutionDate(FindTodoQuery query) {
        try {
            return todoQueryService.findTodoListByManagerIdAndExecutionDate(query.getManagerId(), query.getExecutionDate());
        } catch(Exception e) {
            log.error("To-Do 조회 중 오류발생", e);
            throw e;
        }
    }


    /**
     * To-Do 우선순위를 계산한다.
     * */
    private Priority calculatePriority(List<Todo> todoList, LocalDateTime executionDate) {
        Priority priority;
        if(todoList.size() == 0 || todoList.stream().noneMatch(todo -> todo.isSameExecutionDate(executionDate))) {
            priority = new Priority(Importance.MIDDLE, 0);
        } else {
            // 데이터가 존재할 경우 우선순위로 정렬 후 가장 마지막에 있는 것이 최하위 To-Do 이므로 그 다음 우선순위로 설정한다.
            List<Todo> sameDateTodoList = todoList.stream().filter(todo -> todo.isSameExecutionDate(executionDate)).sorted(Comparator.comparing(Todo::getPriority)).collect(Collectors.toList());

            Todo lowestTodo = sameDateTodoList.get(sameDateTodoList.size() - 1);

            priority = new Priority(lowestTodo.getPriority().getImportance(), lowestTodo.getPriority().getSequence() + 1);
        }
        return priority;
    }

}
