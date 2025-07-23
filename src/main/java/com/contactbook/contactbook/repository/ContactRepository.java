package com.contactbook.contactbook.repository;

import com.contactbook.contactbook.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    // Finds all contacts associated with a specific user's username
    List<Contact> findByUser_Username(String username);
}
