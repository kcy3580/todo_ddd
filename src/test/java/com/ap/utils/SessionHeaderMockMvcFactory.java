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
                .accept(MediaType.APPLICATION_JSON);
    }

    public static MockHttpServletRequestBuilder put(String url, Object... uriVars) {
        return MockMvcRequestBuilders.put(url, uriVars)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

}