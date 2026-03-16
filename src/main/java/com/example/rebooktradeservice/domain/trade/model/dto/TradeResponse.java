package com.example.rebooktradeservice.domain.trade.model.dto;

import com.example.rebooktradeservice.common.enums.State;
import com.example.rebooktradeservice.domain.trade.model.entity.Trade;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record TradeResponse(
    Long tradeId,
    Long bookId,
    String userId,
    String title,
    String content,
    String rating,
    Integer price,
    State state,
    String imageUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    boolean isMarked
) {
    public static TradeResponse from(Trade trade) {
        return TradeResponse.builder()
            .tradeId(trade.getId())
            .bookId(trade.getBookId())
            .userId(trade.getUserId())
            .title(trade.getTitle())
            .content(trade.getContent())
            .rating(trade.getRating())
            .price(trade.getPrice())
            .state(trade.getState())
            .imageUrl(trade.getImageUrl())
            .createdAt(trade.getCreatedAt())
            .updatedAt(trade.getUpdatedAt())
            .isMarked(false)
            .build();
    }

    public TradeResponse withMarked(boolean marked) {
        return TradeResponse.builder()
            .tradeId(tradeId)
            .bookId(bookId)
            .userId(userId)
            .title(title)
            .content(content)
            .rating(rating)
            .price(price)
            .state(state)
            .imageUrl(imageUrl)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .isMarked(marked)
            .build();
    }
}
