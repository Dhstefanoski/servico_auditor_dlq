package com.example.auditor.domain.service;

import com.example.auditor.domain.model.Severity;

public final class SeverityPolicy {

    private SeverityPolicy() {}

    public static Severity classify(int totalAmount) {
        if (totalAmount > 100) {
            return Severity.HIGH;
        }
        if (totalAmount >= 50) {
            return Severity.MEDIUM;
        }
        return Severity.LOW;
    }
}
