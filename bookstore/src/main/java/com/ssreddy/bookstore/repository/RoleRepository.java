package com.ssreddy.bookstore.repository;

import org.springframework.data.repository.CrudRepository;

import com.ssreddy.bookstore.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{

	Role findByName(String name);
	
}
