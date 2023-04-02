package com.ap.todo.interfaces.mapper;

import com.ap.config.MapstructConfig;
import com.ap.todo.domain.aggregates.Todo;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.interfaces.dto.CreateTodoReqDto;
import com.ap.todo.interfaces.dto.CreateTodoRspDto;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(config = MapstructConfig.class)
@Slf4j
public abstract class CreateTodoMapper {

    @Mapping(target = "task", source = "reqDto.task")
    @Mapping(target = "managerId", source = "reqDto.managerId")
    @Mapping(target = "executionDate", ignore = true)
    @Mapping(target = "description", source = "reqDto.description")
    public abstract CreateTodoCommand toCommand(CreateTodoReqDto reqDto);

    @AfterMapping
    protected void afterMappingToCommand(
            @MappingTarget final CreateTodoCommand.CreateTodoCommandBuilder<?,?> targetBuilder,
            CreateTodoReqDto reqDto) {
        LocalDateTime convertedExecutionDate = LocalDateTime.parse(reqDto.getExecutionDate(), DateTimeFormatter.ofPattern("yyyyMMdd"));
        targetBuilder.executionDate(convertedExecutionDate);
    }


    @Mapping(target = "todoId", source = "todo.todoId")
    @Mapping(target = "task", source = "todo.task")
    @Mapping(target = "description", source = "todo.description")
    public abstract CreateTodoRspDto toResponseDto(Todo todo);

}