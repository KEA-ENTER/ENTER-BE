package kea.enter.enterbe.global.algorithm;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CalculateMemberDto {
        private Long id;
        private String name;
        private String email;
        private String reason;
        private Integer oldScore;
        private Integer newScore;

        @Builder
        public CalculateMemberDto(Long id, String name, String email, String reason, Integer oldScore, Integer newScore) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.reason = reason;
            this.oldScore = oldScore;
            this.newScore = newScore;
        }

        public static CalculateMemberDto of(Long id, String name, String email, String reason, Integer oldScore, Integer newScore) {
            return CalculateMemberDto.builder()
                .id(id)
                .name(name)
                .email(email)
                .reason(reason)
                .oldScore(oldScore)
                .newScore(newScore)
                .build();
        }
}
