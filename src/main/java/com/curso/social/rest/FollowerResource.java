package com.curso.social.rest;

import com.curso.social.domain.model.Follower;
import com.curso.social.domain.model.User;
import com.curso.social.domain.repository.FollowerRepository;
import com.curso.social.domain.repository.UserRepository;
import com.curso.social.rest.dto.FollowRequest;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users/{userId}/followers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FollowerResource {
    FollowerRepository followerRepository;
    UserRepository userRepository;

    @Inject
    public FollowerResource(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    @PUT
    @Transactional
    public Response followerUser(@PathParam("userId") Long userId, FollowRequest followRequest) {
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        User follower = userRepository.findById(followRequest.getFollowerId());

        boolean follows = followerRepository.follows(follower, user);
        if (!follows) {
            Follower followers = new Follower();
            followers.setUser(user);
            followers.setFollower(follower);

            followerRepository.persist(followers);

        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

}











