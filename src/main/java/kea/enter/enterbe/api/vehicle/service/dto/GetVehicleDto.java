package kea.enter.enterbe.api.vehicle.service.dto;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kea.enter.enterbe.domain.take.entity.VehicleReport;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleNoteState;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class GetVehicleDto {
    private Long id;

    @Builder
    public GetVehicleDto(Long id) {
        this.id = id;
    }

    public static GetVehicleDto of(Long id) {
        return GetVehicleDto.builder()
            .id(id)
            .build();
    }
}
