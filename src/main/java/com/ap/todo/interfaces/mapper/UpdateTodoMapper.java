package com.ap.todo.interfaces.mapper;

import com.ap.config.MapstructConfig;
import com.ap.todo.constant.Importance;
import com.ap.todo.constant.TodoStatus;
import com.ap.todo.domain.commands.UpdateTodoCommand;
import com.ap.todo.domain.valueobjects.Priority;
import com.ap.todo.interfaces.dto.UpdateTodoReqDto;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Mapper(config = MapstructConfig.class)
@Slf4j
public abstract class UpdateTodoMapper {

    @Mapping(target = "todoId", source = "reqDto.todoId")
    @Mapping(target = "managerIdForDelegation", source = "reqDto.managerIdForDelegation")
    @Mapping(target = "task", source = "reqDto.task")
    @Mapping(target = "description", source = "reqDto.description")
    @Mapping(target = "executionDate", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "status", ignore = true)
    public abstract UpdateTodoCommand toCommand(UpdateTodoReqDto reqDto);

    @AfterMapping
    protected void afterMappingToCommand(
            @MappingTarget final UpdateTodoCommand.UpdateTodoCommandBuilder targetBuilder,
            UpdateTodoReqDto reqDto) {
        if(Objects.nonNull(reqDto.getExecutionDate())) {
            LocalDateTime convertedExecutionDate = LocalDateTime.parse(reqDto.getExecutionDate(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            targetBuilder.executionDate(convertedExecutionDate);
        }
        if(Objects.nonNull(reqDto.getPriority())) {
            targetBuilder.priority(new Priority(Importance.findByCode(reqDto.getPriority().getImportance()), reqDto.getPriority().getSequence()));
        }
        if(Objects.nonNull(reqDto.getStatus())) {
            targetBuilder.status(TodoStatus.findByCode(reqDto.getStatus()));
        }
    }

}