package com.ap.todo.infrastructure.repositories;

import com.ap.common.exception.ApiException;
import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.repositories.TodoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ap.common.constants.ResultCode.NO_DATA;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

    List<Todo> todoList = new ArrayList<>();

    @Override
    public List<Todo> findByManagerId(String managerId) {
        return this.todoList.stream().filter(todo -> todo.getManager().getId().equals(managerId)).collect(Collectors.toList());
    }

    @Override
    public List<Todo> findTodoListByManagerIdAndExecutionDate(String managerId, LocalDateTime executionDate) {
        return this.todoList.stream().filter(todo -> todo.isSameManager(managerId) && todo.isSameExecutionDate(executionDate)).sorted(Comparator.comparing(Todo::getPriority)).collect(Collectors.toList());
    }

    @Override
    public Todo findById(String todoId) {
        Todo targetTodo = this.todoList.stream().filter(todo -> todo.getTodoId() == Long.parseLong(todoId)).findFirst().orElseThrow(() -> new ApiException(NO_DATA));
        deleteById(todoId);     // DB상에서 업데이트 된 것처럼 보이기 위해 삭제 후 add 해준다.
        return targetTodo;
    }

    @Override
    public void save(Todo todo) {
        this.todoList.add(todo);
    }

    @Override
    public void saveAll(List<Todo> todoList) {
        this.todoList.addAll(todoList);
    }

    @Override
    public void deleteById(String todoId) {
        Optional<Todo> todoOptional = this.todoList.stream().filter(todo -> todo.getTodoId() == Long.parseLong(todoId)).findFirst();
        if(todoOptional.isPresent()) {
            this.todoList.remove(todoOptional.get());
        } else {
            throw new ApiException(NO_DATA);
        }
    }

}
