package com.tienda_back.repository.Users;

import com.tienda_back.model.entity.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            select u.*
            from "user" u
            where u.username = :username
            """, nativeQuery = true)
    User findUserByUsername(@Param("username") String username);


    @Query(value = """
            SELECT COUNT(*)
            FROM "user" u\s
            WHERE user_id = :user_Id""", nativeQuery = true)
    int countUserByUserId(@Param("user_Id") Long userId);


    @Query(value = """
            select u.*
            from "user" u
            where u.user_id = :user_id
            """, nativeQuery = true)
    User findUserByUserId(@Param("user_id") Long userId);
}
