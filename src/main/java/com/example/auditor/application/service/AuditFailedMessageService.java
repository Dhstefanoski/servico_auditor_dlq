package com.example.auditor.application.service;

import com.example.auditor.application.port.in.AuditFailedMessageUseCase;
import com.example.auditor.application.port.out.FailedMessageRepositoryPort;
import com.example.auditor.domain.model.FailedMessage;
import com.example.auditor.domain.model.Severity;
import com.example.auditor.domain.service.SeverityPolicy;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditFailedMessageService implements AuditFailedMessageUseCase {

    private static final Logger log = LoggerFactory.getLogger(AuditFailedMessageService.class);

    private final FailedMessageRepositoryPort repository;
    private final ObjectMapper objectMapper;

    public AuditFailedMessageService(FailedMessageRepositoryPort repository,
                                     ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void audit(String rawPayload, String queueName) {
        int totalAmount = extractTotalAmount(rawPayload);
        Severity severity = SeverityPolicy.classify(totalAmount);

        FailedMessage record = FailedMessage.newPending(queueName, rawPayload, severity);
        FailedMessage saved = repository.save(record);

        log.info("Mensagem auditada: errorId={}, queue={}, totalItens={}, severity={}",
                saved.getErrorId(), saved.getQueueName(), totalAmount, saved.getSeverity());
    }

    private int extractTotalAmount(String rawPayload) {
        try {
            JsonNode root = objectMapper.readTree(rawPayload);
            JsonNode items = root.path("orderItems");
            int total = 0;
            if (items.isArray()) {
                for (JsonNode item : items) {
                    total += item.path("amount").asInt(0);
                }
            }
            return total;
        } catch (Exception e) {
            log.warn("Falha ao parsear payload para extrair quantidade. Tratando como severity LOW. Causa: {}",
                    e.getMessage());
            return 0;
        }
    }
}
