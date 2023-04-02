package com.ap.todo.interfaces.mapper;

import com.ap.config.MapstructConfig;
import com.ap.todo.domain.commands.CreateTodoCommand;
import com.ap.todo.interfaces.dto.CreateTodoReqDto;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapstructConfig.class)
@Slf4j
public abstract class CreateTodoMapper {

    @Mapping(target = "task", source = "reqDto.task")
    @Mapping(target = "managerId", source = "reqDto.managerId")
    @Mapping(target = "executionDate", source = "reqDto.executionDate")
    @Mapping(target = "description", source = "reqDto.description")
    public abstract CreateTodoCommand toCommand(CreateTodoReqDto reqDto);

}