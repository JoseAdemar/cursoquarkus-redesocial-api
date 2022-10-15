package com.curso.social.rest.dto;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.ws.rs.core.Response;

import lombok.Data;

@Data
public class ResponseError {
    public static final int UNPROCESSABLE_ENTITY_STATUS = 422;
	private String message;
    private Collection<FieldError> errors;
    
    public ResponseError(String message, Collection<FieldError> errors) {
		this.message = message;
		this.errors = errors;
	}
	
	public static <T> ResponseError createFromValidation(
		Set<ConstraintViolation<T>>violations) {
	    List<FieldError> erros = violations
		.stream()
		.map(constraintViolation -> new FieldError(
				constraintViolation.getPropertyPath().toString(), 
				constraintViolation.getMessage()))
		.collect(Collectors.toList());
	    
	    String message = "Validator error";
	    
	    var responseError = new ResponseError(message, erros);
	    return responseError;
	}
	
	//Criando um status code manualmente
	public Response withStatusCode(int code) {
		return Response.status(code).entity(this).build();
	}
	
 }



	
