package com.curso.social.rest.dto;

import com.curso.social.domain.model.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private String text;
    private LocalDateTime localDateTime;

    public static PostResponse fromEntity(Post post){
        PostResponse postResponse = new PostResponse();
        postResponse.setText(post.getPost_text());
        postResponse.setLocalDateTime(post.getLocalDateTime());
        return postResponse;
    }

}
