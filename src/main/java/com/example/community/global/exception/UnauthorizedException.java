package com.example.community.global.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
  private final Object data;

  public UnauthorizedException(String message, Object data) {
    super("로그인이 되어있지 않습니다. 로그인을 해주세요");
    this.data = data;
  }

}