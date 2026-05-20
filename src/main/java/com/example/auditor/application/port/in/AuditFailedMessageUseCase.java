package com.example.auditor.application.port.in;

public interface AuditFailedMessageUseCase {

    void audit(String rawPayload, String queueName);
}
