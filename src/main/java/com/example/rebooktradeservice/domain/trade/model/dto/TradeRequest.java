package com.example.rebooktradeservice.domain.trade.model.dto;

import com.example.rebooktradeservice.common.enums.State;
import com.example.rebooktradeservice.domain.trade.model.entity.Trade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record TradeRequest(
    @NotNull
    Long bookId,

    @NotBlank
    @Length(min = 3, max = 100)
    String title,

    @NotBlank
    @Length(min = 3, max = 800)
    String content,

    @NotBlank
    @Length(min = 1, max = 5)
    String rating,

    int price,

    @NotNull
    State state
) {
    public Trade toEntity(String imageUrl, String userId) {
        return Trade.builder()
            .bookId(bookId)
            .userId(userId)
            .title(title)
            .content(content)
            .imageUrl(imageUrl)
            .rating(rating)
            .price(price)
            .state(state)
            .build();
    }
}
