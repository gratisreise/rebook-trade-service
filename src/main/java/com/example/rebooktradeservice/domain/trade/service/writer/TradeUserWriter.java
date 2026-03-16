package com.example.rebooktradeservice.domain.trade.service.writer;

import com.example.rebooktradeservice.domain.trade.model.entity.TradeUser;
import com.example.rebooktradeservice.domain.trade.model.entity.compositekey.TradeUserId;
import com.example.rebooktradeservice.domain.trade.repository.TradeUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeUserWriter {

    private final TradeUserRepository tradeUserRepository;

    @Transactional
    public void save(TradeUser tradeUser) {
        tradeUserRepository.save(tradeUser);
    }

    @Transactional
    public void deleteById(TradeUserId tradeUserId) {
        tradeUserRepository.deleteById(tradeUserId);
    }

    @Transactional
    public boolean existsById(TradeUserId tradeUserId) {
        return tradeUserRepository.existsById(tradeUserId);
    }
}
