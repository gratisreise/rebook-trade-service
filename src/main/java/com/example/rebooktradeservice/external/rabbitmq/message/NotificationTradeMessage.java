package com.example.rebooktradeservice.external.rabbitmq.message;

public record NotificationTradeMessage(
    String message,
    String type,
    String tradeId,
    String bookId
) {
    public NotificationTradeMessage(Long tradeId, String content, Long bookId) {
        this(content, "TRADE", tradeId.toString(), bookId.toString());
    }
}
