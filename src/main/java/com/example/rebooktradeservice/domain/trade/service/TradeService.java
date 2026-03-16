package com.example.rebooktradeservice.domain.trade.service;

import com.rebook.common.core.response.PageResponse;
import com.example.rebooktradeservice.common.enums.State;
import com.example.rebooktradeservice.domain.trade.exception.TradeException;
import com.example.rebooktradeservice.clientfeign.book.BookClient;
import com.example.rebooktradeservice.domain.trade.model.dto.TradeRequest;
import com.example.rebooktradeservice.domain.trade.model.dto.TradeResponse;
import com.example.rebooktradeservice.domain.outbox.Outbox;
import com.example.rebooktradeservice.domain.trade.model.entity.Trade;
import com.example.rebooktradeservice.domain.trade.model.entity.compositekey.TradeUserId;
import com.example.rebooktradeservice.external.rabbitmq.message.NotificationTradeMessage;
import com.example.rebooktradeservice.domain.outbox.OutBoxRepository;
import com.example.rebooktradeservice.domain.trade.repository.TradeRepository;
import com.example.rebooktradeservice.domain.trade.repository.TradeUserRepository;
import com.example.rebooktradeservice.external.s3.S3Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final TradeReader tradeReader;
    private final BookClient bookClient;
    private final S3Service s3Service;
    private final TradeUserRepository tradeUserRepository;
    private final OutBoxRepository outBoxRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void postTrade(TradeRequest request, String userId, MultipartFile file) throws IOException {
        String imageUrl = s3Service.upload(file);
        Trade trade = new Trade(request, imageUrl, userId);
        tradeRepository.save(trade);

        String content = "찜한 도서의 새로운 거래가 등록되었습니다.";
        NotificationTradeMessage message = new NotificationTradeMessage(trade.getId(), content, request.bookId());
        saveOutBox(message);
    }

    private void saveOutBox(NotificationTradeMessage message) throws JsonProcessingException {
        String payload = objectMapper.writeValueAsString(message);
        Outbox outBox = new Outbox();
        outBox.setPayload(payload);
        outBoxRepository.save(outBox);
    }

    public TradeResponse getTrade(String userId, Long tradeId) {
        Trade trade = tradeReader.findById(tradeId);
        TradeResponse response = new TradeResponse(trade);
        return checkMarking(response, userId);
    }

    @Transactional
    public void updateState(Long tradeId, State state, String userId) {
        Trade trade = tradeReader.findById(tradeId);
        if (!trade.getUserId().equals(userId)) {
            log.error("Unauthorized updateTrade Access");
            throw TradeException.unauthorized("Unauthorized user Access");
        }
        trade.setState(state);
    }

    @Transactional
    public void updateTrade(TradeRequest request, String userId, Long tradeId, MultipartFile file)
        throws IOException {
        Trade trade = tradeReader.findById(tradeId);
        if (!trade.getUserId().equals(userId)) {
            log.error("Unauthorized updateState Access");
            throw TradeException.unauthorized("Unauthorized user Access");
        }

        if (request.price() != trade.getPrice()) {
            String content = "찜한 제품의 가격이 변동되었습니다.";
            NotificationTradeMessage message =
                new NotificationTradeMessage(tradeId, content, request.bookId());
            saveOutBox(message);
        }

        if (file != null) {
            String imageUrl = s3Service.upload(file);
            trade.setImageUrl(imageUrl);
        }

        trade.update(request, userId);
    }

    public PageResponse<TradeResponse> getTrades(String userId, Pageable pageable) {
        Page<Trade> trades = tradeReader.readTrades(userId, pageable);
        Page<TradeResponse> responses = trades.map(TradeResponse::new)
            .map(res -> checkMarking(res, userId));
        return PageResponse.from(responses);
    }

    @Transactional
    public void deleteTrade(Long tradeId, String userId) {
        if (!tradeRepository.existsById(tradeId)) {
            log.error("Data is not found");
            throw TradeException.notFound("Data is not found");
        }

        Trade trade = tradeReader.findById(tradeId);

        if (!trade.getUserId().equals(userId)) {
            log.error("Unauthorized deleteTrade Access");
            throw TradeException.unauthorized("Unauthorized user Access");
        }

        tradeRepository.deleteById(tradeId);
    }

    public PageResponse<TradeResponse> getAllTrades(String userId, Long bookId, Pageable pageable) {
        Page<Trade> trades = tradeReader.getAllTrades(bookId, pageable);
        Page<TradeResponse> responses = trades.map(TradeResponse::new)
            .map(res -> checkMarking(res, userId));
        return PageResponse.from(responses);
    }

    public PageResponse<TradeResponse> getRecommendations(String userId, Pageable pageable) {
        List<Long> bookIds = bookClient.getRecommendedBooks(userId);
        log.info("Recommendations: {}", bookIds.toString());

        if (bookIds.isEmpty())
            return PageResponse.from(Page.empty());

        Page<Trade> trades = tradeReader.getRecommendations(bookIds, pageable);
        Page<TradeResponse> responses = trades.map(TradeResponse::new)
            .map(res -> checkMarking(res, userId));
        return PageResponse.from(responses);
    }

    private TradeResponse checkMarking(TradeResponse res, String userId) {
        long tradeId = res.tradeId();
        TradeUserId tradeUserId = new TradeUserId(tradeId, userId);
        if (tradeUserRepository.existsByTradeUserId(tradeUserId)) {
            return res.withMarked(true);
        }
        return res;
    }

    public PageResponse<TradeResponse> getOthersTrades(String userId, Pageable pageable) {
        return tradeReader.getOthersTrades(userId, pageable);
    }
}
