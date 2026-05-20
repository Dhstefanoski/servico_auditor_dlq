package com.example.auditor.domain.model;

import java.time.Instant;
import java.util.UUID;

public class FailedMessage {

    private final UUID errorId;
    private final String queueName;
    private final String payload;
    private final Instant timestamp;
    private final AuditStatus status;
    private final Severity severity;

    public FailedMessage(UUID errorId,
                         String queueName,
                         String payload,
                         Instant timestamp,
                         AuditStatus status,
                         Severity severity) {
        this.errorId = errorId;
        this.queueName = queueName;
        this.payload = payload;
        this.timestamp = timestamp;
        this.status = status;
        this.severity = severity;
    }

    public static FailedMessage newPending(String queueName,
                                           String payload,
                                           Severity severity) {
        return new FailedMessage(
                UUID.randomUUID(),
                queueName,
                payload,
                Instant.now(),
                AuditStatus.PENDING_ANALYSIS,
                severity
        );
    }

    public UUID getErrorId() { return errorId; }
    public String getQueueName() { return queueName; }
    public String getPayload() { return payload; }
    public Instant getTimestamp() { return timestamp; }
    public AuditStatus getStatus() { return status; }
    public Severity getSeverity() { return severity; }
}
