package com.example.auditor.domain.service;

import com.example.auditor.domain.model.Severity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeverityPolicyTest {

    @Test
    void shouldReturnLowWhenLessThan50() {
        assertEquals(Severity.LOW, SeverityPolicy.classify(0));
        assertEquals(Severity.LOW, SeverityPolicy.classify(49));
    }

    @Test
    void shouldReturnMediumBetween50And100Inclusive() {
        assertEquals(Severity.MEDIUM, SeverityPolicy.classify(50));
        assertEquals(Severity.MEDIUM, SeverityPolicy.classify(75));
        assertEquals(Severity.MEDIUM, SeverityPolicy.classify(100));
    }

    @Test
    void shouldReturnHighWhenGreaterThan100() {
        assertEquals(Severity.HIGH, SeverityPolicy.classify(101));
        assertEquals(Severity.HIGH, SeverityPolicy.classify(1000));
    }
}
