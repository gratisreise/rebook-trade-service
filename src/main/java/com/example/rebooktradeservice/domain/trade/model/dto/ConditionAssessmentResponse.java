package com.example.rebooktradeservice.domain.trade.model.dto;

import com.example.rebooktradeservice.common.enums.BookCondition;
import com.example.rebooktradeservice.common.enums.State;

public record ConditionAssessmentResponse(
    Long tradeId,
    BookCondition condition,
    State state
) {}
