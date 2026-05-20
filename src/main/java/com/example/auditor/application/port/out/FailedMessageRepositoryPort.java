package com.example.auditor.application.port.out;

import com.example.auditor.domain.model.FailedMessage;

public interface FailedMessageRepositoryPort {

    FailedMessage save(FailedMessage failedMessage);
}
