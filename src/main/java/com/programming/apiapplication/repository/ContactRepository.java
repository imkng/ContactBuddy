package com.programming.apiapplication.repository;

import com.programming.apiapplication.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByUserId(Long id, Pageable pageable);

    Optional<Contact> findByUserIdAndId(Long id, Long id1);
}
