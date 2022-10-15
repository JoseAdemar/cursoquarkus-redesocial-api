package com.curso.social.rest;

import java.util.Set;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.curso.social.domain.model.User;
import com.curso.social.domain.repository.UserRepository;
import com.curso.social.rest.dto.CreateUserRequest;
import com.curso.social.rest.dto.ResponseError;

import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class UserResource {
	
	UserRepository repository;
	Validator validator;
	
	@Inject
	public UserResource(UserRepository repository, Validator validator) {
		this.repository = repository;
		this.validator = validator;
	}

	@POST
	@Transactional
	public Response createUser(CreateUserRequest userRequest) {
		
		Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);
		if(!violations.isEmpty()) {
			
			return ResponseError
					.createFromValidation(violations)
					.withStatusCode(ResponseError.UNPROCESSABLE_ENTITY_STATUS);
		}

		User user = new User();
		user.setName(userRequest.getName());
		user.setAge(userRequest.getAge());

		repository.persist(user);

		return Response.status(Status.CREATED).entity(user).build();

	}

	@GET
	public Response getAllUsers() {
		PanacheQuery<User> query = repository.findAll();
		return Response.ok(query.list()).build();
	}

	@DELETE
	@Path("{id}")
	@Transactional
	public Response deleteUser(@PathParam("id") Long id) {
		User user = repository.findById(id);
		if (user != null) {
			repository.delete(user);
			return Response.status(Status.NO_CONTENT).build();
		}

		return Response.status(Status.NOT_FOUND).build();
	}
	
	@PUT
	@Path("{id}")
	@Transactional
	public Response updateUser(@PathParam("id") Long id, CreateUserRequest createUserRequest) {
		User user = repository.findById(id);
		if (user != null) {
			user.setName(createUserRequest.getName());
			user.setAge(createUserRequest.getAge());
			return Response.status(Status.OK).build();
		}

		return Response.status(Status.NOT_FOUND).build();
	}
}
