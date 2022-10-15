package com.curso.social.domain.repository;

import javax.enterprise.context.ApplicationScoped;

import com.curso.social.domain.model.Post;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class PostRepository implements PanacheRepositoryBase<Post, Long>{

}
