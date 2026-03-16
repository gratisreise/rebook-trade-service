package com.example.rebooktradeservice.domain.trade.service.writer;

import com.example.rebooktradeservice.domain.trade.model.entity.Trade;
import com.example.rebooktradeservice.domain.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeWriter {

    private final TradeRepository tradeRepository;

    @Transactional
    public Trade save(Trade trade) {
        return tradeRepository.save(trade);
    }

    @Transactional
    public void deleteById(Long tradeId) {
        tradeRepository.deleteById(tradeId);
    }
}
