package kea.enter.enterbe.api.apply.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class GetApplyVehicleResponse {
    private String competitionRate;
    private String model;
    private String fuel;
    private String company;
    private int seat;
    private MultipartFile img;

    @Builder
    public GetApplyVehicleResponse(String competitionRate, String model, String fuel,
        String company, int seat, MultipartFile img){
        this.competitionRate = competitionRate;
        this.model = model;
        this.fuel = fuel;
        this.company = company;
        this.seat = seat;
        this.img = img;
    }

    public GetApplyVehicleResponse of(String competitionRate, String model, String fuel,
        String company, int seat, MultipartFile img){
        return GetApplyVehicleResponse.builder()
            .competitionRate(competitionRate)
            .model(model)
            .fuel(fuel)
            .company(company)
            .seat(seat)
            .img(img)
            .build();
    }

}
