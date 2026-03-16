package com.example.rebooktradeservice.domain.trade.service.reader;

import com.example.rebooktradeservice.domain.trade.model.entity.Trade;
import com.example.rebooktradeservice.domain.trade.model.entity.compositekey.TradeUserId;
import com.example.rebooktradeservice.domain.trade.repository.TradeUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TradeUserReader {

    private final TradeUserRepository tradeUserRepository;

    public boolean isMarked(TradeUserId tradeUserId) {
        return tradeUserRepository.existsByTradeUserId(tradeUserId);
    }

    public Page<Trade> findMarkedTradesByUserId(String userId, Pageable pageable) {
        return tradeUserRepository.findTradeByUserId(userId, pageable);
    }
}
