package com.curso.social.rest;

import com.curso.social.domain.model.Post;
import com.curso.social.domain.model.User;
import com.curso.social.domain.repository.PostRepository;
import com.curso.social.domain.repository.UserRepository;
import com.curso.social.rest.dto.CreatePostRequest;
import com.curso.social.rest.dto.PostResponse;
import io.quarkus.panache.common.Sort;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Path("/users/{userId}/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

	private PostRepository postRepository;
	private UserRepository userRepository;

	@Inject
	public PostResource(PostRepository postRepository, UserRepository userRepository) {
		this.postRepository = postRepository;
		this.userRepository = userRepository;
	}

	@POST
	@Transactional
	public Response savePost(@PathParam("userId") Long userId, CreatePostRequest createPostRequest) {
		User user = userRepository.findById(userId);
		if (user == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		Post post = new Post();
		post.setPost_text(createPostRequest.getPost_text());
		post.setLocalDateTime(LocalDateTime.now());
		post.setUser(user);

		postRepository.persist(post);

		return Response.status(Status.CREATED).build();
	}

	@GET
	public Response listPosts(@PathParam("userId") Long userId) {
		User user = userRepository.findById(userId);
		if(user == null){
			return Response.status(Status.NOT_FOUND).build();
		}
		 var query =  postRepository.find("user", Sort.descending("localDateTime"),user);
		 var list = query.list();
		 var postResponseList = list.stream()
				 .map(post -> PostResponse.fromEntity(post))
				 .collect(Collectors.toList());
		return Response.status(Status.OK).entity(postResponseList).build();
	}
}
