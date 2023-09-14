package com.zeroonedance.adminapi.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     * 根据给定的用户名查找用户信息。
     *
     * @param username 用户名
     * @return 可能存在或不存在一个User对象的Optional
     */
    Optional<User> findUserByUsername(String username);


}
