package com.springboot.user_app.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ResponseStructure<T> {
	private T data;
	private LocalDateTime time;
	private String message;
	private int httpStatusCode;

}
