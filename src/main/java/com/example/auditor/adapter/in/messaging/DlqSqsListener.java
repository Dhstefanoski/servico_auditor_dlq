package com.example.auditor.adapter.in.messaging;

import com.example.auditor.application.port.in.AuditFailedMessageUseCase;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DlqSqsListener {

    private static final Logger log = LoggerFactory.getLogger(DlqSqsListener.class);

    private final AuditFailedMessageUseCase useCase;

    @Value("${queue.dlq-name}")
    private String dlqName;

    public DlqSqsListener(AuditFailedMessageUseCase useCase) {
        this.useCase = useCase;
    }

    @SqsListener("${queue.dlq-name}")
    public void onDlqMessage(String rawPayload) {
        log.info("Mensagem recebida da DLQ [{}]: {}", dlqName, rawPayload);
        useCase.audit(rawPayload, dlqName);
    }
}
