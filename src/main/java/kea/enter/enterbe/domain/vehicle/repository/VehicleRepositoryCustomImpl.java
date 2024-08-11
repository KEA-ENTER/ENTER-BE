package kea.enter.enterbe.domain.vehicle.repository;

import static kea.enter.enterbe.domain.apply.entity.QApply.apply;
import static kea.enter.enterbe.domain.lottery.entity.QWinning.winning;
import static kea.enter.enterbe.domain.member.entity.QMember.member;
import static kea.enter.enterbe.domain.take.entity.QVehicleReport.vehicleReport;
import static kea.enter.enterbe.domain.vehicle.entity.QVehicle.vehicle;
import static kea.enter.enterbe.domain.vehicle.entity.QVehicleNote.vehicleNote;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kea.enter.enterbe.api.vehicle.controller.dto.response.AdminVehicleResponse;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
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
            .where(vehicle.vehicleNo.contains(vehicleNo), vehicle.model.contains(model), vehicle.state.eq(state));

        List<Vehicle> vehicleList = this.getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(vehicleList, pageable, query.fetchCount());
    }

    @Override
    public AdminVehicleResponse findAdminVehicleResponsebyId(Long id) {
        // 차량 정보
        Vehicle v = queryFactory.selectFrom(vehicle).where(vehicle.id.eq(id)).fetchFirst();
        if (v == null)
            throw new CustomException(ResponseCode.VEHICLE_NOT_VALID);


        // 차량 특이사항
        List<Tuple> results = queryFactory.select(
                member.name, vehicleNote.createdAt, vehicleNote.content)
            .from(vehicle)
            .leftJoin(vehicleNote).on(vehicle.id.eq(vehicleNote.vehicle.id))
            .leftJoin(vehicleReport).on(vehicleNote.vehicleReport.id.eq(vehicleReport.id))
            .leftJoin(winning).on(vehicleReport.winning.id.eq(winning.id))
            .leftJoin(apply).on(winning.apply.id.eq(apply.id))
            .leftJoin(member).on(apply.member.id.eq(member.id))
            .where(vehicle.id.eq(id))
            .fetch();

        List<String> names = new ArrayList<>();
        List<String> reportCreatedAts = new ArrayList<>();
        List<String> contents = new ArrayList<>();

        for (Tuple tuple : results) {
            String name = tuple.get(member.name);
            String noteCreatedAt = localDateTimeToString(tuple.get(vehicleNote.createdAt));
            String content = tuple.get(vehicleNote.content);

            if (name != null && noteCreatedAt != null && content != null) {
                names.add(name);
                reportCreatedAts.add(noteCreatedAt);
                contents.add(content);
            }
        }

        return AdminVehicleResponse.builder()
            .vehicleId(v.getId())
            .vehicleNo(v.getVehicleNo())
            .company(v.getCompany())
            .model(v.getModel())
            .seats(v.getSeats())
            .fuel(v.getFuel())
            .img(v.getImg())
            .createdAt(localDateTimeToString(v.getCreatedAt()))
            .updatedAt(localDateTimeToString(v.getUpdatedAt()))
            .state(v.getState())
            .names(names)
            .reportCreatedAts(reportCreatedAts)
            .contents(contents)
            .build();
    }

    private String localDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return null;
        else
            return localDateTime.toLocalDate().toString();
    }
}