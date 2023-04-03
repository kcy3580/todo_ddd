package com.ap.todo.application.commandservices;

import com.ap.todo.application.outboundservices.ManagerOutboundService;
import com.ap.todo.application.queryservices.TodoQueryService;
import com.ap.todo.constant.Importance;
import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.commands.UpdateTodoCommand;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.domain.queries.FindTodoQuery;
import com.ap.todo.domain.repositories.TodoRepository;
import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.domain.valueobjects.Priority;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
            Todo todo = new Todo((long) todoList.size() + 1, command, manager, calculatePriority(todoList, command.getExecutionDate(), Importance.MIDDLE));

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
     * To-Do 내용을 변경한다.
     * @param command   변경 command
     * */
    public void updateInfo(UpdateTodoCommand command) {
        List<Todo> updatingTargetList = new ArrayList<>();
        Todo todo = todoQueryService.findById(command.getTodoId());

        // 담당자 위임 정보 존재 시 담당자 조회 후 위임을 진행한다.(description에 위임 정보를 기록한다.)
        if(StringUtils.isNotEmpty(command.getManagerIdForDelegation())) {
            Todo delegatedTodo = SerializationUtils.clone(todo);
            Manager manager = managerOutboundService.findManagerInfo(command.getManagerIdForDelegation());
            List<Todo> todoList = todoQueryService.findTodoListByManagerId(manager.getId());
            // 위임받은 To-Do는 기본적으로 A0의 우선순위를 가지고 해당 순위 존재 시 하위 우선순위로 배정된다.(현재 리스트에 1을 더한 값이 todoId 값이 된다.)
            delegatedTodo.updatePriorityAndStatusByDelegation(
                    todoList.size() + 1,
                    calculatePriority(todoList, delegatedTodo.getExecutionDate(),
                            Importance.MIDDLE_HIGH), manager
            );

            // 위임자와 위임 받은 서로에게 정보를 기록한다.
            delegatedTodo.updateDelegationInfo("담당자" + " " + todo.getManager().getName() + "로 부터 위임받음");
            todo.updateDelegationInfo("위임" + "(" + "담당자" + " " + manager.getName() + ")");
            updatingTargetList.add(delegatedTodo);
            updatingTargetList.add(todo);
        } else {
            todo.updateInfo(command);
            updatingTargetList.add(todo);
        }
        todoRepository.saveAll(updatingTargetList);
    }

    /**
     * To-Do를 todoId 기준으로 삭제한다.
     * @param todoId     todoId
     * */
    public void deleteByTodoId(String todoId) {
        try {
            todoRepository.deleteById(todoId);
        } catch(Exception e) {
            log.error("To-Do 삭제 중 오류발생", e);
            throw e;
        }
    }

    /**
     * To-Do 우선순위를 계산한다.
     * */
    private Priority calculatePriority(List<Todo> todoList, LocalDateTime executionDate, Importance importance) {
        Priority priority;
        if(todoList.size() == 0 || todoList.stream().noneMatch(todo -> todo.isSameExecutionDate(executionDate) && todo.isSameImportance(importance))) {
            priority = new Priority(importance, 0);
        } else {
            // 데이터가 존재할 경우 우선순위로 정렬 후 가장 마지막에 있는 것이 최하위 To-Do 이므로 그 다음 우선순위로 설정한다.
            List<Todo> sameDateTodoList = todoList.stream().filter(todo -> todo.isSameExecutionDate(executionDate)).sorted(Comparator.comparing(Todo::getPriority)).collect(Collectors.toList());

            Todo lowestTodo = sameDateTodoList.get(sameDateTodoList.size() - 1);

            priority = new Priority(lowestTodo.getPriority().getImportance(), lowestTodo.getPriority().getSequence() + 1);
        }
        return priority;
    }

}
