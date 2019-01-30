package com.ssreddy.bookstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.ssreddy.bookstore.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUserName(String userName);

	User findByEmail(String email);
}
