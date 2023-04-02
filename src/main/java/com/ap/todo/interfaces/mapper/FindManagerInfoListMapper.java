package com.ap.todo.interfaces.mapper;

import com.ap.config.MapstructConfig;
import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.interfaces.dto.FindMangerInfoRspDto;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(config = MapstructConfig.class)
@Slf4j
public abstract class FindManagerInfoListMapper {

    public abstract List<FindMangerInfoRspDto> toListResponseDto(List<Manager> managerInfoList);

    @Mapping(target = "id", source = "managerInfo.id")
    @Mapping(target = "name", source = "managerInfo.name")
    public abstract FindMangerInfoRspDto toResponseDto(Manager managerInfo);

}