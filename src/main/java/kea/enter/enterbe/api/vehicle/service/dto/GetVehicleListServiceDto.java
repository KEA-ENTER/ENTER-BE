package kea.enter.enterbe.api.vehicle.service.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
public class GetVehicleListServiceDto {
    private String word;
    private String searchCategory;
    private Pageable pageable;

    @Builder
    public GetVehicleListServiceDto(String word, String searchCategory, Pageable pageable) {
        this.word = word;
        this.searchCategory = searchCategory;
        this.pageable = pageable;
    }

    public static GetVehicleListServiceDto of(String word, String searchCategory, Pageable pageable) {
        return GetVehicleListServiceDto.builder()
            .word(word)
            .searchCategory(searchCategory)
            .pageable(pageable)
            .build();
    }
}
