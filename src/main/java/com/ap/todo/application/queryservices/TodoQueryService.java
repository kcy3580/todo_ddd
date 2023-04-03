package com.ap.todo.application.queryservices;

import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoQueryService {

    private final TodoRepository todoRepository;

    /**
     * 담당자 ID 기준으로 To-Do 목록을 가져온다.
     * */
    public List<Todo> findTodoListByManagerId(String managerId) {
        return todoRepository.findByManagerId(managerId);
    }

    /**
     * 담당자 ID와 실행날짜 기준으로 To-Do 목록을 가져온다.
     * */
    public List<Todo> findTodoListByManagerIdAndExecutionDate(String managerId, LocalDateTime executionDate) {
        return todoRepository.findTodoListByManagerIdAndExecutionDate(managerId, executionDate);
    }

    /**
     * To-Do Id 기준으로 정보를 가져온다.
     * */
    public Todo findById(String todoId) {
        return todoRepository.findById(todoId);
    }

}
