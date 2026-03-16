package com.example.rebooktradeservice.domain.trade.repository;

import com.example.rebooktradeservice.domain.trade.model.entity.Trade;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {

    Page<Trade> findByUserId(String userId, Pageable pageable);

    Page<Trade> findByBookId(Long bookId, Pageable pageable);

    Page<Trade> findByBookIdIn(List<Long> bookIds, Pageable pageable);
}
