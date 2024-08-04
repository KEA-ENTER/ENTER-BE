package kea.enter.enterbe.api.vehicle.service;

import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleResponse;
import kea.enter.enterbe.api.vehicle.service.dto.CreateVehicleDto;
import kea.enter.enterbe.api.vehicle.service.dto.GetVehicleListDto;
import kea.enter.enterbe.api.vehicle.service.dto.ModifyVehicleDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public interface AdminVehicleService {
    Page<AdminVehicleResponse> getVehicleList(Pageable pageable, GetVehicleListDto getVehicleListDto);
    void createVehicle(CreateVehicleDto dto);
    void modifyVehicle(ModifyVehicleDto dto);
}
