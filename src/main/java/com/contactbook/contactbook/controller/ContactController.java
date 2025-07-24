package com.contactbook.contactbook.controller;

import com.contactbook.contactbook.entity.Contact;
import com.contactbook.contactbook.entity.User;
import com.contactbook.contactbook.repository.ContactRepository;
import com.contactbook.contactbook.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    private Optional<User> getCurrentUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername());
    }

    @GetMapping
    public ResponseEntity<List<Contact>> getAllContacts(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(contactRepository.findByUser_Username(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> currentUserOpt = getCurrentUser(userDetails);
        if (currentUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        contact.setUser(currentUserOpt.get());
        Contact savedContact = contactRepository.save(contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long id, @RequestBody Contact contactDetails, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Contact> contactOpt = contactRepository.findById(id);
        if (contactOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Contact contact = contactOpt.get();
        if (!contact.getUser().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // The syntax error was an extra '}' before this line. It has been removed.

        contact.setName(contactDetails.getName());
        contact.setPhoneNumber(contactDetails.getPhoneNumber());
        contact.setEmail(contactDetails.getEmail());
        final Contact updatedContact = contactRepository.save(contact);
        return ResponseEntity.ok(updatedContact);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Contact> contactOpt = contactRepository.findById(id);
        if (contactOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Contact contact = contactOpt.get();
        if (!contact.getUser().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        // The syntax error was an extra '}' before this line. It has been removed.

        contactRepository.delete(contact);
        return ResponseEntity.noContent().build();
    }
}
