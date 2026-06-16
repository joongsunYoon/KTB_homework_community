package com.example.community.global;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private String code;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", data);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>("SUCCESS", null);
    }

    public static ApiResponse<Void> fail(String code) {
        return new ApiResponse<>(code, null);
    }

}
