package com.example.rebooktradeservice.domain.trade.model.entity;

import com.example.rebooktradeservice.domain.trade.model.entity.compositekey.TradeUserId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TradeUser {
    @EmbeddedId
    private TradeUserId tradeUserId;

    @MapsId("tradeId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trade_id")
    private Trade trade;
}
