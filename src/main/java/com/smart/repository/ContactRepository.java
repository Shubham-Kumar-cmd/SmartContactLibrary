package com.smart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smart.model.Contact;
import com.smart.model.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	//making this repository for pagination
	@Query("select c from Contact c where c.user.user_id=:i")
	public Page<Contact> findContactByUserEmail(@Param("i") int userId,Pageable pageable);
	//Pageable contain two information 
	//1)currentpage page
	//2)contact per page
	
	
	//search 
	public List<Contact> findByNameContainingAndUser(String keyword,User user);//it will find all the contacts of current user and restrict to search other users contact
}
