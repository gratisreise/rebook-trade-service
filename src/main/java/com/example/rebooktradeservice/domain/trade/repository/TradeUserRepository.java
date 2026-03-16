package com.example.rebooktradeservice.domain.trade.repository;

import com.example.rebooktradeservice.domain.trade.model.entity.Trade;
import com.example.rebooktradeservice.domain.trade.model.entity.TradeUser;
import com.example.rebooktradeservice.domain.trade.model.entity.compositekey.TradeUserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeUserRepository extends JpaRepository<TradeUser, TradeUserId> {

    @Query("select tu.trade from TradeUser tu where tu.tradeUserId.userId = :userId")
    Page<Trade> findTradeByUserId(String userId, Pageable pageable);

    boolean existsByTradeUserId(TradeUserId tradeUserId);
}
