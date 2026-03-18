package com.example.rebooktradeservice.common.enums;

public enum State {
    WAITING,    // Initial state for trades pending AI assessment
    AVAILABLE,  // Trade is active after AI assessment
    SOLD,
    RESERVED,
    ;
}
