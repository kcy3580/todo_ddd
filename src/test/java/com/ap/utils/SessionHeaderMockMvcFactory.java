package com.ap.utils;


import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class SessionHeaderMockMvcFactory {

    public static MockHttpServletRequestBuilder post(String url, Object... uriVars) {
        return MockMvcRequestBuilders.post(url, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder get(String url, Object... uriVars) {
        return MockMvcRequestBuilders.get(url, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("manager-id", "tester");
    }

    public static MockHttpServletRequestBuilder getForNoManagerId(String url, Object... uriVars) {
        return MockMvcRequestBuilders.get(url, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder patch(String url, Object... uriVars) {
        return MockMvcRequestBuilders.patch(url, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder delete(String url, Object... uriVars) {
        return MockMvcRequestBuilders.delete(url, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

}