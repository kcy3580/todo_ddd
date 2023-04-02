package com.ap.todo.application.queryservices;

import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.domain.repositories.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TodoQueryService {

    private final TodoRepository todoRepository;

    /**
     * 담당자 ID로 To-Do 목록을 가져온다.
     * */
    public List<Todo> findTodoList(String managerId) {
        return todoRepository.findByManagerId(managerId);
    }

}
