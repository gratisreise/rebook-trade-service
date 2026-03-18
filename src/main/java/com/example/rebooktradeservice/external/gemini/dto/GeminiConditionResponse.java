package com.example.rebooktradeservice.external.gemini.dto;

import com.example.rebooktradeservice.common.enums.BookCondition;

public record GeminiConditionResponse(
    BookCondition condition
) {}
