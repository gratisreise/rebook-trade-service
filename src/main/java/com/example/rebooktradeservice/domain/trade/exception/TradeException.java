package com.example.rebooktradeservice.domain.trade.exception;

import com.rebook.common.core.exception.BusinessException;
import com.rebook.common.core.exception.ErrorCode;

/**
 * Trade 도메인 관련 예외
 */
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
}
