package com.ap.todo.constant;

public class TodoApiUrl {

    private TodoApiUrl(){}

    /** To-Do 기본 URL */
    public static final String TODO_BASE_URL = "/todo/v1";

    /** To-Do 생성 */
    public static final String CREATE_TODO_URL = "/todos";

    /** To-Do 조회 */
    public static final String FIND_TODO_URL = "/todos";

    /** To-Do 목록 조회 */
    public static final String FIND_MANAGER_INFO_LIST_URL = "/managers/list";

    /** To-Do 변경 */
    public static final String UPDATE_TODO_URL = "/todos";

    /** To-Do 삭제 */
    public static final String DELETE_TODO_URL = "/todos/{todoId}";

}