package com.ap.todo.infrastructure.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FindManagerInfoDto {

    // 담당자 Id
    private String id;

    // 이름
    private String name;

}
