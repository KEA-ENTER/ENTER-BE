package kea.enter.enterbe.global.algorithm;

import java.util.List;
import lombok.Getter;

@Getter
public class CalculateDto {
    List<CalculateMemberDto> updates;

    public CalculateDto(List<CalculateMemberDto> updates) {
        this.updates = updates;
    }

    public static CalculateDto of(List<CalculateMemberDto> updates) {
        return new CalculateDto(updates);
    }

}
