package com.example.auditor.adapter.out.persistence;

import com.example.auditor.application.port.out.FailedMessageRepositoryPort;
import com.example.auditor.domain.model.FailedMessage;
import org.springframework.stereotype.Component;

@Component
public class FailedMessagePersistenceAdapter implements FailedMessageRepositoryPort {

    private final SpringDataFailedMessageRepository jpaRepository;

    public FailedMessagePersistenceAdapter(SpringDataFailedMessageRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public FailedMessage save(FailedMessage failedMessage) {
        FailedMessageEntity entity = toEntity(failedMessage);
        FailedMessageEntity persisted = jpaRepository.save(entity);
        return toDomain(persisted);
    }

    private FailedMessageEntity toEntity(FailedMessage m) {
        return new FailedMessageEntity(
                m.getErrorId(),
                m.getQueueName(),
                m.getPayload(),
                m.getTimestamp(),
                m.getStatus(),
                m.getSeverity()
        );
    }

    private FailedMessage toDomain(FailedMessageEntity e) {
        return new FailedMessage(
                e.getErrorId(),
                e.getQueueName(),
                e.getPayload(),
                e.getTimestamp(),
                e.getStatus(),
                e.getSeverity()
        );
    }
}
