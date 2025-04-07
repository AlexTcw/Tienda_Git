package com.tienda_back.repository;

import com.tienda_back.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
                   select u.*
                   from "user" as u
                   where u.username = :username
                   """,nativeQuery = true)
    User findUserByUsername(@Param("username") String username);
}
