package kea.enter.enterbe.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum ResponseCode {
    //MEMBER
    MEMBER_NOT_FOUND("MEM-ERR-001", HttpStatus.NOT_FOUND, "멤버를 찾을 수 없습니다."),
    //APPLY
    APPLY_NOT_FOUND("APL-ERR-001", HttpStatus.NOT_FOUND ,"지원 정보를 찾을 수 없습니다."),
    //WINNING
    WINNING_NOT_FOUND("WIN-ERR-001", HttpStatus.NOT_FOUND,"당첨 정보를 찾을 수 없습니다."),

    // APPLY_ROUND
    APPLY_ROUND_NOT_FOUND("APR-ERR-001", HttpStatus.NOT_FOUND, "신청 회차를 찾을 수 없습니다."),

    //VEHICLE
    VEHICLE_NOT_VALID("VHC_ERR_001", HttpStatus.NOT_FOUND, "법인 차량 정보를 찾을 수 없습니다."),
    VEHICLE_NO_NOT_ALLOWED("VHC_ERR_002", HttpStatus.METHOD_NOT_ALLOWED, "유효하지 않은 차량번호 형식입니다."),
    VEHICLE_DUPLICATED("VHC_ERR_003", HttpStatus.BAD_REQUEST, "이미 존재하는 차량입니다."),
    NEED_PARKING_LOC_FOR_RETURN_REPORT("VHC-ERR-004",HttpStatus.BAD_REQUEST , "주차 위치를 입력해주세요."),
  
    // PENALTY
    PENALTY_NOT_FOUND("PEN-ERR-001", HttpStatus.NOT_FOUND, "페널티를 찾을 수 없습니다."),

    // AUTh
    PASSWORD_INCORRECT("AUT-ERR-001", HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다"),
    ALREADY_LOGGED_IN("AUT-ERR-002", HttpStatus.BAD_REQUEST, "이미 로그인 되어있습니다."),
    NOT_FOUND_USER("AUT-ERR-003", HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    NOT_FOUND_REFRESH_TOKEN("AUT-ERR-004", HttpStatus.NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다."),
    NOT_FOUND_ACCESS_TOKEN("AUT-ERR-005", HttpStatus.NOT_FOUND, "액세스 토큰을 찾을 수 없습니다."),
    INVALID_TOKEN("AUT-ERR-006", HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN("AUT-ERR-007", HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN("AUT-ERR-008", HttpStatus.UNAUTHORIZED, "지원되지 않는 토큰입니다."),
    INVALID_HEADER_OR_COMPACT_JWT("AUT-ERR-009", HttpStatus.UNAUTHORIZED, "헤더 또는 컴팩트 JWT가 잘못되었습니다."),

    //GLOBAL
    BAD_REQUEST("GLB-ERR-001", HttpStatus.NOT_FOUND, "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED("GLB-ERR-002", HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR("GLB-ERR-003", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),

    // Question
    NOT_FOUND_MEMBER("MEM-ERR-001", HttpStatus.NOT_FOUND, "멤버가 존재하지 않습니다."),
    NOT_FOUND_QUESTION("QST-ERR-001", HttpStatus.NOT_FOUND, "문의사항이 존재하지 않습니다."),
    INVALID_QUESTION_STATE("QST-ERR-002", HttpStatus.INTERNAL_SERVER_ERROR, "수정할 수 없는 문의 사항입니다."),

    NOT_IMAGE_FILE("GLB-ERR-004", HttpStatus.BAD_REQUEST, "이미지 파일이 아닙니다.");


    private final String code;
    private final HttpStatus status;
    private final String message;


    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
            .filter(Predicate.not(String::isBlank))
            .orElse(this.getMessage());
    }
}
