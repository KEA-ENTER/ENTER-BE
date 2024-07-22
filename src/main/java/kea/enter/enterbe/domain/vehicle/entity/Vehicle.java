package kea.enter.enterbe.domain.vehicle.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import kea.enter.enterbe.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vehicle")
public class Vehicle extends BaseEntity {
    @Column(name = "car_number", nullable = false)
    private int carNumber;

    @Column(name = "manufacturing_company", nullable = false)
    private String manufacturingCompany;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "seats", nullable = false)
    private int seats;

    @Column(name = "fuel", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleFuel fuel;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleState state;

    @Builder
    public Vehicle(int carNumber, String manufacturingCompany, String model, int seats,
        VehicleFuel fuel, String image, VehicleState state) {
        this.carNumber = carNumber;
        this.manufacturingCompany = manufacturingCompany;
        this.model = model;
        this.seats = seats;
        this.fuel = fuel;
        this.image = image;
        this.state = state;
    }

    public static Vehicle of(int carNumber, String manufacturingCompany, String model, int seats,
        VehicleFuel fuel, String image, VehicleState state) {
        return Vehicle.builder()
            .carNumber(carNumber)
            .manufacturingCompany(manufacturingCompany)
            .model(model)
            .seats(seats)
            .fuel(fuel)
            .image(image)
            .state(state)
            .build();
    }
}
