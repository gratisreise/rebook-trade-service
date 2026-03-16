package com.example.rebooktradeservice.domain.trade.service;

import com.example.rebooktradeservice.domain.trade.exception.TradeException;
import com.example.rebooktradeservice.domain.trade.model.dto.TradeResponse;
import com.example.rebooktradeservice.domain.trade.model.entity.Trade;
import com.example.rebooktradeservice.domain.trade.model.entity.TradeUser;
import com.example.rebooktradeservice.domain.trade.model.entity.compositekey.TradeUserId;
import com.example.rebooktradeservice.domain.trade.repository.TradeRepository;
import com.example.rebooktradeservice.domain.trade.repository.TradeUserRepository;
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

    private final TradeUserRepository tradeUserRepository;
    private final TradeRepository tradeRepository;


    @Transactional
    public void tradeMark(String userId, Long tradeId) {
        TradeUserId tradeUserId = new TradeUserId(tradeId, userId);
        if (!tradeRepository.existsById(tradeId)) {
            tradeUserRepository.deleteById(tradeUserId);
            return;
        }
        if (tradeUserRepository.existsById(tradeUserId)) {
            tradeUserRepository.deleteById(tradeUserId);
        } else {
            Trade trade = tradeRepository.findById(tradeId)
                .orElseThrow(() -> TradeException.notFound("Trade not found with id: " + tradeId));
            TradeUser tradeUser = new TradeUser(tradeUserId, trade);
            tradeUserRepository.save(tradeUser);
        }
    }

    public Page<TradeResponse> getMarkedTrades(String userId, Pageable pageable) {
        Page<Trade> markedTrades = tradeUserRepository.findTradeByUserId(userId, pageable);
        return markedTrades.map(TradeResponse::new);
    }
}
