package com.curso.social.domain.repository;

import com.curso.social.domain.model.Follower;
import com.curso.social.domain.model.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class FollowerRepository implements PanacheRepositoryBase<Follower, Long> {
    public boolean follows(User follower, User user){
        Map params = Parameters.with("follower", follower)
                .and("user", user).map();

        PanacheQuery<Follower> query = find("follower = :follower and user = :user ", params);
        Optional<Follower> result = query.firstResultOptional();

        return  result.isPresent();
    }

    public List<Follower> findByUser(Long userId) {
        PanacheQuery<Follower> query = find("user.id", userId);
        return query.list();
    }

    public void deleteByFollowerAndUser(Long followerId, Long userId) {
        var params = Parameters
                .with("userId", userId)
                .and("followerId", followerId).map();
        delete("follower.id =:followerId and user.id =:userId", params);
    }
}
