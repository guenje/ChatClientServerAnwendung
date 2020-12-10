package com.chatserver.persistence;

import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * Repository for User objects.
 */
@Repository
public class SimpleUserRepository {
    public List<User> findAll(){
        return Arrays.asList(
                new User("test1", "123456789"),
                new User("test2", "123456789"),
                new User("test3", "123456789"),
                new User("test4", "123456789"),
                new User("test5", "123456789"),
                new User("test6", "123456789"),
                new User("test7", "123456789"),
                new User("test8", "123456789"),
                new User("test9", "123456789"),
                new User("test10", "123456789"),
                new User("test11", "123456789"),
                new User("test12", "123456789")
        );
    }
}
