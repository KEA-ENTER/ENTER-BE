package kea.enter.enterbe.domain.vehicle.repository;

import static kea.enter.enterbe.domain.apply.entity.QApply.apply;
import static kea.enter.enterbe.domain.member.entity.QMember.member;
import static kea.enter.enterbe.domain.lottery.entity.QWinning.winning;
import static kea.enter.enterbe.domain.take.entity.QVehicleReport.vehicleReport;
import static kea.enter.enterbe.domain.vehicle.entity.QVehicle.vehicle;
import static kea.enter.enterbe.domain.vehicle.entity.QVehicleNote.vehicleNote;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleResponse;
import kea.enter.enterbe.domain.lottery.entity.Winning;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class VehicleRepositoryCustomImpl extends QuerydslRepositorySupport implements VehicleRepositoryCustom {
    @Autowired
    private JPAQueryFactory queryFactory;

    public VehicleRepositoryCustomImpl() {
        super(Vehicle.class);
    }

    @Override
    public Page<Vehicle> findBySearchOption(Pageable pageable, String vehicleNo, String model, VehicleState state) {
        JPQLQuery<Vehicle> query =  queryFactory.selectFrom(vehicle)
            .where(likeNo(vehicleNo), likeModel(model), eqState(state));

        List<Vehicle> vehicleList = this.getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(vehicleList, pageable, query.fetchCount());
    }

    @Override
    public AdminVehicleResponse findAdminVehicleResponsebyId(Long id) {
        // query
        List<Tuple> results =  queryFactory.select(
                vehicle.id, vehicle.vehicleNo, vehicle.company, vehicle.model, vehicle.seats,
                vehicle.fuel, vehicle.img, vehicle.createdAt, vehicle.updatedAt, vehicle.state,
                member.name, vehicleNote.createdAt, vehicleNote.content)
            .from(vehicle)
            .leftJoin(vehicleNote).on(vehicle.id.eq(vehicleNote.vehicle.id))
            .leftJoin(vehicleReport).on(vehicleNote.vehicleReport.id.eq(vehicleReport.id))
            .leftJoin(winning).on(vehicleReport.winning.id.eq(winning.id))
            .leftJoin(apply).on(winning.apply.id.eq(apply.id))
            .leftJoin(member).on(apply.member.id.eq(member.id))
            .where(vehicle.id.eq(id))
            .fetch();

        // 차량 정보 저장
        Long vehicleId = results.get(0).get(vehicle.id);
        String vehicleNo = results.get(0).get(vehicle.vehicleNo);
        String company = results.get(0).get(vehicle.company);
        String model = results.get(0).get(vehicle.model);
        int seats = results.get(0).get(vehicle.seats);
        VehicleFuel fuel = results.get(0).get(vehicle.fuel);
        String img = results.get(0).get(vehicle.img);
        String createdAt = results.get(0).get(vehicle.createdAt).toLocalDate().toString();
        String updatedAt = results.get(0).get(vehicle.updatedAt).toLocalDate().toString();
        VehicleState state = results.get(0).get(vehicle.state);

        // 차량 특이사항 저장
        List<String> names = new ArrayList<>();
        List<String> reportCreatedAts = new ArrayList<>();
        List<String> contents = new ArrayList<>();

        for (Tuple tuple : results) {
            String name = tuple.get(member.name);
            String noteCreatedAt = tuple.get(vehicleNote.createdAt).toLocalDate().toString();
            String content = tuple.get(vehicleNote.content);

            if (name != null && noteCreatedAt != null && content != null) {
                names.add(name);
                reportCreatedAts.add(noteCreatedAt);
                contents.add(content);
            }
        }

        return AdminVehicleResponse.builder()
            .vehicleId(vehicleId)
            .vehicleNo(vehicleNo)
            .company(company)
            .model(model)
            .seats(seats)
            .fuel(fuel)
            .img(img)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .state(state)
            .names(names)
            .reportCreatedAts(reportCreatedAts)
            .contents(contents)
            .build();
    }


    private BooleanExpression likeNo(String vehicleNo) {
        if(vehicleNo == null || vehicleNo.isEmpty()) {
            return null;
        }
        return vehicle.vehicleNo.containsIgnoreCase(vehicleNo);
    }

    private BooleanExpression likeModel(String model) {
        if(model == null || model.isEmpty()) {
            return null;
        }
        return vehicle.model.containsIgnoreCase(model);
    }

    private BooleanExpression eqState(VehicleState state) {
        if(state == null) {
            return null;
        }
        return vehicle.state.eq(state);
    }
}
