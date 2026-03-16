package com.example.rebooktradeservice.domain.trade.controller;

import com.rebook.common.core.response.SuccessResponse;
import com.example.rebooktradeservice.domain.trade.model.dto.TradeResponse;
import com.example.rebooktradeservice.domain.trade.service.TradeUserService;
import com.rebook.common.auth.PassportUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trades")
public class TradeUserController {

    private final TradeUserService tradeUserService;

    @PostMapping("/{tradeId}/marks")
    public ResponseEntity<Void> tradeMark(@PassportUser String userId, @PathVariable Long tradeId) {
        tradeUserService.tradeMark(userId, tradeId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/marks")
    public ResponseEntity<SuccessResponse<Page<TradeResponse>>> getMarkedTrades(
        @PassportUser String userId,
        @PageableDefault Pageable pageable
    ) {
        return SuccessResponse.toOk(tradeUserService.getMarkedTrades(userId, pageable));
    }
}
