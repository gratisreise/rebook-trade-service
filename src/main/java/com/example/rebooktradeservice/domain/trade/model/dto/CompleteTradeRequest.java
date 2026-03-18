package com.example.rebooktradeservice.domain.trade.model.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CompleteTradeRequest(
    @NotBlank @Length(min = 3, max = 100) String title,
    @NotBlank @Length(min = 3, max = 800) String content
) {}
