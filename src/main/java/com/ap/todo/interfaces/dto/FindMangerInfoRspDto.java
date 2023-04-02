package com.ap.todo.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FindMangerInfoRspDto {

    // 담당자 Id
    private String id;

    // 이름
    private String name;

}
