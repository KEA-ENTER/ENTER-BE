package kea.enter.enterbe.global.algorithm;

import kea.enter.enterbe.domain.lottery.entity.Waiting;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LotteryMemberDto {
    private Long id;
    private String name;
    private String email;

    @Builder
    public LotteryMemberDto(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public static LotteryMemberDto of(Long id, String name, String email) {
        return LotteryMemberDto.builder()
            .id(id)
            .name(name)
            .email(email)
            .build();
    }


    public static LotteryMemberDto of(Winning winning) {
        return LotteryMemberDto.builder()
            .id(winning.getApply().getMember().getId())
            .name(winning.getApply().getMember().getName())
            .email(winning.getApply().getMember().getEmail())
            .build();
    }
    public static LotteryMemberDto of(Waiting waiting) {
        return LotteryMemberDto.builder()
            .id(waiting.getApply().getMember().getId())
            .name(waiting.getApply().getMember().getName())
            .email(waiting.getApply().getMember().getEmail())
            .build();
    }


}
