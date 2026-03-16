package com.example.rebooktradeservice.domain.trade.service;

import com.rebook.common.core.response.PageResponse;
import com.example.rebooktradeservice.domain.trade.exception.TradeException;
import com.example.rebooktradeservice.domain.trade.model.dto.TradeResponse;
import com.example.rebooktradeservice.domain.trade.model.entity.Trade;
import com.example.rebooktradeservice.domain.trade.repository.TradeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TradeReader {
    private final TradeRepository tradeRepository;

    public Trade findById(Long tradeId) {
        return tradeRepository.findById(tradeId)
            .orElseThrow(() -> TradeException.notFound("Trade not found with id: " + tradeId));
    }

    public Page<Trade> readTrades(String userId, Pageable pageable) {
        return tradeRepository.findByUserId(userId, pageable);
    }

    public Page<Trade> getAllTrades(Long bookId, Pageable pageable) {
        return tradeRepository.findByBookId(bookId, pageable);
    }

    public Page<Trade> getRecommendations(List<Long> bookIds, Pageable pageable) {
        return tradeRepository.findByBookIdIn(bookIds, pageable);
    }

    public PageResponse<TradeResponse> getOthersTrades(String userId, Pageable pageable) {
        Page<Trade> trades = tradeRepository.findByUserId(userId, pageable);
        Page<TradeResponse> responses = trades.map(TradeResponse::new);
        return PageResponse.from(responses);
    }
}
