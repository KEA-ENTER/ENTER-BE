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
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "vehicle")
public class Vehicle extends BaseEntity {
    @Column(name = "vehicle_no", nullable = false)
    private String vehicleNo;

    @Column(name = "company", nullable = false)
    private String company;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "seats", nullable = false)
    private int seats;

    @Column(name = "fuel", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleFuel fuel;

    @Column(name = "img")
    private String img;

    @Setter
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleState state;

    @Builder
    public Vehicle(String vehicleNo, String company, String model, int seats, VehicleFuel fuel,
        String img, VehicleState state) {
        this.vehicleNo = vehicleNo;
        this.company = company;
        this.model = model;
        this.seats = seats;
        this.fuel = fuel;
        this.img = img;
        this.state = state;
    }

    public static Vehicle of(String vehicleNo, String company, String model, int seats,
        VehicleFuel fuel, String img, VehicleState state) {
        return Vehicle.builder()
            .vehicleNo(vehicleNo)
            .company(company)
            .model(model)
            .seats(seats)
            .fuel(fuel)
            .img(img)
            .state(state)
            .build();
    }

    public void patchVehicle(String vehicleNo, String company, String model, int seats, VehicleFuel fuel,
        String img, VehicleState state) {
        this.vehicleNo = vehicleNo;
        this.company = company;
        this.model = model;
        this.seats = seats;
        this.fuel = fuel;
        this.img = img;
        this.state = state;
    }

    public void deleteVehicle() {
        this.state = VehicleState.INACTIVE;
    }

}
