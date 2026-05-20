package com.example.auditor.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataFailedMessageRepository extends JpaRepository<FailedMessageEntity, UUID> {
}
