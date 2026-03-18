package com.example.rebooktradeservice.domain.trade.model.dto;

import java.util.List;

public record BundleTradeResponse(
    Integer totalCount,
    List<CreatedTradeItem> trades
) {
    public record CreatedTradeItem(
        Long tradeId,
        Long bookId,
        String imageUrl
    ) {}
}
