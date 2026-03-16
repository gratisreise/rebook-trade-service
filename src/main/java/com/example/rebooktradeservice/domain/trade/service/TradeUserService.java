package com.example.rebooktradeservice.domain.trade.service;

import com.example.rebooktradeservice.common.exception.TradeException;
import com.example.rebooktradeservice.domain.trade.model.dto.TradeResponse;
import com.example.rebooktradeservice.domain.trade.model.entity.Trade;
import com.example.rebooktradeservice.domain.trade.model.entity.TradeUser;
import com.example.rebooktradeservice.domain.trade.model.entity.compositekey.TradeUserId;
import com.example.rebooktradeservice.domain.trade.service.reader.TradeReader;
import com.example.rebooktradeservice.domain.trade.service.reader.TradeUserReader;
import com.example.rebooktradeservice.domain.trade.service.writer.TradeUserWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeUserService {

    private final TradeUserReader tradeUserReader;
    private final TradeUserWriter tradeUserWriter;
    private final TradeReader tradeReader;

    @Transactional
    public void tradeMark(String userId, Long tradeId) {
        TradeUserId tradeUserId = new TradeUserId(tradeId, userId);

        // 1. Trade 존재 여부 확인
        if (!tradeReader.existsById(tradeId)) {
            log.warn("Trade not found, removing bookmark if exists: tradeId={}", tradeId);
            tradeUserWriter.deleteById(tradeUserId);
            return;
        }

        // 2. 이미 찜한 경우 -> 찜 취소
        if (tradeUserWriter.existsById(tradeUserId)) {
            tradeUserWriter.deleteById(tradeUserId);
            log.info("Bookmark removed: userId={}, tradeId={}", userId, tradeId);
        }
        // 3. 찜하지 않은 경우 -> 찜 추가
        else {
            Trade trade = tradeReader.findById(tradeId);
            TradeUser tradeUser = new TradeUser(tradeUserId, trade);
            tradeUserWriter.save(tradeUser);
            log.info("Bookmark added: userId={}, tradeId={}", userId, tradeId);
        }
    }


    public Page<TradeResponse> getMarkedTrades(String userId, Pageable pageable) {
        // 1. 찜한 Trade 목록 조회
        Page<Trade> trades = tradeUserReader.findMarkedTradesByUserId(userId, pageable);

        // 2. DTO 변환 (찜한 목록이므로 isMarked = true)
        return trades.map(trade -> new TradeResponse(trade).withMarked(true));
    }
}
