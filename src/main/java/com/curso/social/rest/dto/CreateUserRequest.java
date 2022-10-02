package com.curso.social.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class CreateUserRequest {

	 @NotBlank(message = "Name is riquired ")
	 private String name;
	 
	 @NotNull(message = "Age can not be null")
	 private Integer age;
}
