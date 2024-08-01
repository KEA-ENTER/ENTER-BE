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
    MEMBER_NOT_FOUND("MEM-ERR-001", HttpStatus.BAD_REQUEST, "멤버를 찾을 수 없습니다."),
    LICENSE_NOT_FOUND("MEM-ERR-002", HttpStatus.BAD_REQUEST, "면허 정보를 찾을 수 없습니다."),
    LICENSE_VALIDATION_FALSE("MEM-ERR-003", HttpStatus.BAD_REQUEST, "면허 진위여부 검사가 필요합니다."),
    LICENSE_AUTHENTICITY_INCORRECT("MEM-ERR-004", HttpStatus.BAD_REQUEST, "면허 정보가 유효하지 않습니다."),
    //APPLY
    APPLY_NOT_FOUND("APL-ERR-001",HttpStatus.BAD_REQUEST ,"지원 정보를 찾을 수 없습니다."),
    //WINNING
    WINNING_NOT_FOUND("WIN-ERR-001",HttpStatus.BAD_REQUEST,"당첨 정보를 찾을 수 없습니다."),
    //GLOBAL
    BAD_REQUEST("GLB-ERR-001", HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    METHOD_NOT_ALLOWED("GLB-ERR-002", HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR("GLB-ERR-003", HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다.");

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
