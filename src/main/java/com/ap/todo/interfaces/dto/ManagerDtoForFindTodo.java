package com.ap.todo.interfaces.dto;

import com.ap.todo.domain.valueobjects.Manager;
import com.ap.todo.domain.valueobjects.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ManagerDtoForFindTodo {

    // 담당자 Id
    private String id;

    // 이름
    private String name;

}
