package com.example.rebooktradeservice.domain.trade.service;

import com.example.rebooktradeservice.common.enums.BookCondition;
import com.example.rebooktradeservice.common.enums.State;
import com.example.rebooktradeservice.common.exception.TradeException;
import com.example.rebooktradeservice.domain.trade.model.dto.ConditionAssessmentResponse;
import com.example.rebooktradeservice.domain.trade.model.entity.Trade;
import com.example.rebooktradeservice.domain.trade.service.reader.TradeReader;
import com.example.rebooktradeservice.domain.trade.service.writer.TradeWriter;
import com.example.rebooktradeservice.external.gemini.GeminiService;
import com.example.rebooktradeservice.external.gemini.ImageSource;
import com.example.rebooktradeservice.external.gemini.dto.GeminiConditionResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConditionAssessmentService {

    private static final int REQUIRED_IMAGE_COUNT = 3;
    private static final String GEMINI_PROMPT = """
        다음 3장의 책 이미지를 분석하여 책의 상태를 평가해주세요.

        평가 기준:
        - BEST (최상): 새 책 수준, 표면 손상 없음, 페이지 상태 양호
        - GOOD (상): 약간의 사용감 있으나 전반적으로 양호
        - MEDIUM (중): 눈에 띄는 사용감, 약간의 손상 있음
        - POOR (하): 심한 사용감, 눈에 띄는 손상 있음

        반드시 다음 JSON 형식으로만 응답하세요 (다른 텍스트 금지):
        {
            "condition": "BEST" 또는 "GOOD" 또는 "MEDIUM" 또는 "POOR"
        }
        """;

    private final GeminiService geminiService;
    private final TradeReader tradeReader;
    private final TradeWriter tradeWriter;

    @Transactional
    public ConditionAssessmentResponse assessCondition(Long tradeId, String userId, List<MultipartFile> files)
        throws IOException {
        // 1. 이미지 개수 검증 (정확히 3장 필요)
        validateImageCount(files);

        // 2. Trade 조회 및 권한 검증
        Trade trade = tradeReader.findById(tradeId);
        validateOwnership(trade, userId);
        validateWaitingState(trade);

        // 3. 이미지를 ImageSource로 변환
        List<ImageSource> imageSources = convertToImageSources(files);

        // 4. Gemini API 호출
        GeminiConditionResponse response = callGeminiForConditionAssessment(imageSources);
        BookCondition condition = response.condition();

        // 5. Trade 상태 및 rating 업데이트
        tradeWriter.updateConditionAndState(tradeId, condition, State.AVAILABLE);

        log.info("Trade {} condition assessed as {} and state changed to AVAILABLE", tradeId, condition);

        return new ConditionAssessmentResponse(tradeId, condition, State.AVAILABLE);
    }

    private void validateImageCount(List<MultipartFile> files) {
        if (files == null || files.size() != REQUIRED_IMAGE_COUNT) {
            throw TradeException.invalidImageCount(
                "Exactly " + REQUIRED_IMAGE_COUNT + " images are required for condition assessment");
        }
    }

    private void validateOwnership(Trade trade, String userId) {
        if (!trade.getUserId().equals(userId)) {
            throw TradeException.unauthorized("Unauthorized user Access");
        }
    }

    private void validateWaitingState(Trade trade) {
        if (trade.getState() != State.WAITING) {
            throw TradeException.invalidStateTransition(
                "Trade must be in WAITING state for assessment. Current state: " + trade.getState());
        }
    }

    private List<ImageSource> convertToImageSources(List<MultipartFile> files) throws IOException {
        return files.stream()
            .map(file -> {
                try {
                    return ImageSource.of(file.getBytes(), file.getContentType());
                } catch (IOException e) {
                    throw TradeException.s3UploadFailed("Failed to read image file: " + e.getMessage());
                }
            })
            .toList();
    }

    private GeminiConditionResponse callGeminiForConditionAssessment(List<ImageSource> imageSources) {
        try {
            return geminiService.callObjectWithImages(GEMINI_PROMPT, imageSources, GeminiConditionResponse.class);
        } catch (Exception e) {
            log.error("Gemini API call failed: {}", e.getMessage());
            throw TradeException.aiAssessmentFailed("Failed to assess book condition: " + e.getMessage());
        }
    }
}
