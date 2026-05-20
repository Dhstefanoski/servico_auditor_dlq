package com.example.auditor.adapter.out.persistence;

import com.example.auditor.domain.model.AuditStatus;
import com.example.auditor.domain.model.Severity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "failed_messages")
public class FailedMessageEntity {

    @Id
    @Column(name = "error_id", nullable = false, updatable = false)
    private UUID errorId;

    @Column(name = "queue_name", nullable = false)
    private String queueName;

    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AuditStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private Severity severity;

    public FailedMessageEntity() {}

    public FailedMessageEntity(UUID errorId, String queueName, String payload,
                               Instant timestamp, AuditStatus status, Severity severity) {
        this.errorId = errorId;
        this.queueName = queueName;
        this.payload = payload;
        this.timestamp = timestamp;
        this.status = status;
        this.severity = severity;
    }

    public UUID getErrorId() { return errorId; }
    public String getQueueName() { return queueName; }
    public String getPayload() { return payload; }
    public Instant getTimestamp() { return timestamp; }
    public AuditStatus getStatus() { return status; }
    public Severity getSeverity() { return severity; }
}
