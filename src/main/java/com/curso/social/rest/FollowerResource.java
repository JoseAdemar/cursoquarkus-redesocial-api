package com.curso.social.rest;

import com.curso.social.domain.model.Follower;
import com.curso.social.domain.model.User;
import com.curso.social.domain.repository.FollowerRepository;
import com.curso.social.domain.repository.UserRepository;
import com.curso.social.rest.dto.FollowRequest;
import com.curso.social.rest.dto.FollowerResponse;
import com.curso.social.rest.dto.FollowersPerUserResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

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
        if(userId.equals(followRequest.getFollowerId())){
           return Response.status(Response.Status.CONFLICT).entity("You can't follow yourself").build();
        }
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

    @GET
    public Response listFollowers(@PathParam("userId") Long userId){
        User user = userRepository.findById(userId);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        var list = followerRepository.findByUser(userId);
        FollowersPerUserResponse responseObject = new FollowersPerUserResponse();
        responseObject.setFollowersCount(list.size());

        var followerList = list.stream().map(FollowerResponse::new)
                .collect(Collectors.toList());

        responseObject.setContent(followerList);
        return Response.ok(responseObject).build();
    }
    @DELETE
    @Transactional
    public Response unfollower(@PathParam("userId") Long userId, @QueryParam("followerId") Long followerId){
        var user = userRepository.findById(userId);
        if (user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        followerRepository.deleteByFollowerAndUser(followerId, userId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}











