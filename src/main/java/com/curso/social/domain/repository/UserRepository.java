package com.curso.social.domain.repository;

import javax.enterprise.context.ApplicationScoped;

import com.curso.social.domain.model.User;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, Long> {

}
