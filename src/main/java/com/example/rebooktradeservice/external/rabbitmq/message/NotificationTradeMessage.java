package com.example.rebooktradeservice.external.rabbitmq.message;

public record NotificationTradeMessage(
    String message,
    String type,
    String tradeId,
    String bookId
) {
    public static NotificationTradeMessage of(Long tradeId, String content, Long bookId) {
        return new NotificationTradeMessage(content, "TRADE", tradeId.toString(), bookId.toString());
    }
}
