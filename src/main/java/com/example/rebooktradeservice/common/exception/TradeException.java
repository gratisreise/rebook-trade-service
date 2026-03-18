package com.example.rebooktradeservice.common.exception;

import com.rebook.common.core.exception.BusinessException;
import com.rebook.common.core.exception.ErrorCode;

public class TradeException extends BusinessException {

    private TradeException(ErrorCode code) {
        super(code);
    }

    // 데이터를 찾을 수 없음
    public static TradeException notFound(String message) {
        return new TradeException(ErrorCode.UNKNOWN_ERROR);
    }

    // 권한 없음
    public static TradeException unauthorized(String message) {
        return new TradeException(ErrorCode.UNKNOWN_ERROR);
    }

    // S3 업로드 실패
    public static TradeException s3UploadFailed(String message) {
        return new TradeException(ErrorCode.FILE_STORAGE_ERROR);
    }

    // 잘못된 상태 전환
    public static TradeException invalidStateTransition(String message) {
        return new TradeException(ErrorCode.UNKNOWN_ERROR);
    }

    // AI 평가 실패
    public static TradeException aiAssessmentFailed(String message) {
        return new TradeException(ErrorCode.EXTERNAL_API_ERROR);
    }

    // 이미지 개수 오류
    public static TradeException invalidImageCount(String message) {
        return new TradeException(ErrorCode.UNKNOWN_ERROR);
    }

    // 입력값 오류
    public static TradeException invalidInput(String message) {
        return new TradeException(ErrorCode.UNKNOWN_ERROR);
    }
}
