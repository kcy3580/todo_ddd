package com.ap.todo.interfaces.mapper;

import com.ap.config.MapstructConfig;
import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.domain.queries.FindTodoQuery;
import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.domain.valueobjects.Priority;
import com.ap.todo.interfaces.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(config = MapstructConfig.class)
@Slf4j
public abstract class FindTodoMapper {

    @Mapping(target = "managerId", source = "managerId")
    @Mapping(target = "executionDate", ignore = true)
    public abstract FindTodoQuery toQuery(String managerId, String executionDate);

    @AfterMapping
    protected void afterMappingToCommand(
            @MappingTarget final FindTodoQuery.FindTodoQueryBuilder targetBuilder,
            String managerId, String executionDate) {
        LocalDateTime convertedExecutionDate = LocalDateTime.parse(executionDate, DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        targetBuilder.executionDate(convertedExecutionDate);
    }


    public abstract List<FindTodoRspDto> toResponseDtoList(List<Todo> todoList);

    @Mapping(target = "todoId", source = "todo.todoId")
    @Mapping(target = "executionDate", ignore = true)
    @Mapping(target = "task", source = "todo.task")
    @Mapping(target = "description", source = "todo.description")
    @Mapping(target = "status", source = "todo.status")
    public abstract FindTodoRspDto toFindTodoResponseDto(Todo todo);

    @AfterMapping
    protected void afterMappingToResponseDto(
            @MappingTarget final FindTodoRspDto.FindTodoRspDtoBuilder targetBuilder,
            Todo todo) {
        targetBuilder.executionDate(todo.getExecutionDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
    }

    @Mapping(target = "id", source = "manager.id")
    @Mapping(target = "name", source = "manager.name")
    public abstract ManagerDtoForFindTodo toManagerDtoForFindTodo(Manager manager);

    @Mapping(target = "importance", ignore = true)
    @Mapping(target = "sequence", source = "priority.sequence")
    public abstract PriorityDtoForFindTodo toPriorityDtoForFindTodo(Priority priority);

    @AfterMapping
    protected void afterMappingToPriorityDtoForFindTodo(
            @MappingTarget final PriorityDtoForFindTodo.PriorityDtoForFindTodoBuilder targetBuilder,
            Priority priority) {
        targetBuilder.importance(priority.getImportance().getCode());
    }
}