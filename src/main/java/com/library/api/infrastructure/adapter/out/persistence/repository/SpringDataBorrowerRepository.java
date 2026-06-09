package com.library.api.infrastructure.adapter.out.persistence.repository;

import com.library.api.infrastructure.adapter.out.persistence.entity.BorrowerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataBorrowerRepository extends JpaRepository<BorrowerEntity, String> {
   boolean existsByEmail(String email);
}