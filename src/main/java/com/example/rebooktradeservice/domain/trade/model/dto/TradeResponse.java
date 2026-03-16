package com.example.rebooktradeservice.domain.trade.model.dto;

import com.example.rebooktradeservice.common.enums.State;
import com.example.rebooktradeservice.domain.trade.model.entity.Trade;
import java.time.LocalDateTime;

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
    public TradeResponse(Trade trade) {
        this(
            trade.getId(),
            trade.getBookId(),
            trade.getUserId(),
            trade.getTitle(),
            trade.getContent(),
            trade.getRating(),
            trade.getPrice(),
            trade.getState(),
            trade.getImageUrl(),
            trade.getCreatedAt(),
            trade.getUpdatedAt(),
            false
        );
    }

    public TradeResponse withMarked(boolean marked) {
        return new TradeResponse(
            tradeId,
            bookId,
            userId,
            title,
            content,
            rating,
            price,
            state,
            imageUrl,
            createdAt,
            updatedAt,
            marked
        );
    }
}
