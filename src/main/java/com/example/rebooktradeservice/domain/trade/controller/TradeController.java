package com.example.rebooktradeservice.domain.trade.controller;

import com.rebook.common.auth.PassportProto.Passport;
import com.rebook.common.core.response.PageResponse;
import com.rebook.common.core.response.SuccessResponse;
import com.example.rebooktradeservice.common.enums.State;
import com.example.rebooktradeservice.domain.trade.model.dto.TradeRequest;
import com.example.rebooktradeservice.domain.trade.model.dto.TradeResponse;
import com.rebook.common.auth.PassportUser;
import com.example.rebooktradeservice.domain.trade.service.TradeReader;
import com.example.rebooktradeservice.domain.trade.service.TradeService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trades")
public class TradeController {

    private final TradeService tradeService;
    private final TradeReader tradeReader;


    @GetMapping("/test")
    public String test(@PassportUser Passport passport) {
        return passport.toString();
    }

    @PostMapping
    public ResponseEntity<Void> postTrade(
        @PassportUser String userId,
        @RequestPart TradeRequest request,
        @RequestPart MultipartFile file)
        throws IOException {
        tradeService.postTrade(request, userId, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{tradeId}")
    public ResponseEntity<SuccessResponse<TradeResponse>> getTrade(
        @PassportUser String userId, @PathVariable Long tradeId) {
        return SuccessResponse.toOk(tradeService.getTrade(userId, tradeId));
    }

    @PatchMapping("/{tradeId}")
    public ResponseEntity<Void> updateState(
        @PathVariable Long tradeId,
        @RequestParam State state,
        @PassportUser String userId
    ) {
        tradeService.updateState(tradeId, state, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{tradeId}")
    public ResponseEntity<Void> updateTrade(
        @PathVariable Long tradeId, @PassportUser String userId,
        @RequestPart TradeRequest request,
        @RequestPart(required = false) MultipartFile file
    ) throws IOException {
        tradeService.updateTrade(request, userId, tradeId, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<PageResponse<TradeResponse>>> getTrades(
        @PassportUser String userId, @PageableDefault Pageable pageable
    ) {
        return SuccessResponse.toOk(tradeService.getTrades(userId, pageable));
    }

    @DeleteMapping("/{tradeId}")
    public ResponseEntity<Void> deleteTrade(@PathVariable Long tradeId, @PassportUser String userId) {
        tradeService.deleteTrade(tradeId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<SuccessResponse<PageResponse<TradeResponse>>> getAllTrades(
        @PassportUser String userId,
        @PathVariable Long bookId, @PageableDefault Pageable pageable) {
        return SuccessResponse.toOk(tradeService.getAllTrades(userId, bookId, pageable));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<SuccessResponse<PageResponse<TradeResponse>>> getRecommendations(
        @PassportUser String userId, @PageableDefault Pageable pageable
    ) {
        return SuccessResponse.toOk(tradeService.getRecommendations(userId, pageable));
    }

    @GetMapping("/others/{userId}")
    public ResponseEntity<SuccessResponse<PageResponse<TradeResponse>>> getOthersTrades(
        @PathVariable String userId,
        @PageableDefault Pageable pageable
    ) {
        return SuccessResponse.toOk(tradeService.getOthersTrades(userId, pageable));
    }
}
