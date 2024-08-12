package kea.enter.enterbe.domain.vehicle.repository;

import static kea.enter.enterbe.domain.apply.entity.QApply.apply;
import static kea.enter.enterbe.domain.lottery.entity.QWinning.winning;
import static kea.enter.enterbe.domain.member.entity.QMember.member;
import static kea.enter.enterbe.domain.take.entity.QVehicleReport.vehicleReport;
import static kea.enter.enterbe.domain.vehicle.entity.QVehicle.vehicle;
import static kea.enter.enterbe.domain.vehicle.entity.QVehicleNote.vehicleNote;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kea.enter.enterbe.api.vehicle.controller.dto.request.VehicleSearchCategory;
import kea.enter.enterbe.api.vehicle.controller.dto.response.GetAdminVehicleResponse;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

@Slf4j
public class VehicleRepositoryCustomImpl extends QuerydslRepositorySupport implements VehicleRepositoryCustom {
    @Autowired
    private JPAQueryFactory queryFactory;

    public VehicleRepositoryCustomImpl() {
        super(Vehicle.class);
    }

    @Override
    public Page<Vehicle> findBySearchOption(Pageable pageable, VehicleSearchCategory searchCategory, String word) {
        BooleanBuilder builder = new BooleanBuilder();

        switch (searchCategory) {
            case ALL -> builder.or(vehicle.vehicleNo.contains(word)
                .or(vehicle.model.contains(word)));

            case VEHICLENO -> builder.and(vehicle.vehicleNo.contains(word));

            case MODEL -> builder.and(vehicle.model.contains(word));

            case STATE -> builder.and(vehicle.state.eq(toVehicleState(word)));
        }

        // state가 INACTIVE가 아닌 vehicle 조회
        builder.and(vehicle.state.ne(VehicleState.INACTIVE));

        // vehicle list
        List<Vehicle> vehicleList = queryFactory.selectFrom(vehicle)
            .where(builder)
            .orderBy(vehicle.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        if (vehicleList.isEmpty() && searchCategory.toString().equals("ALL")) {
            vehicleList = queryFactory.selectFrom(vehicle)
                .where(vehicle.state.eq(toVehicleState(word))
                    .and(vehicle.state.ne(VehicleState.INACTIVE)))
                .orderBy(vehicle.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        }

        // 항목 수
        JPQLQuery<Vehicle> countQuery = queryFactory.selectFrom(vehicle)
            .where(builder);
        long totalCount = countQuery.fetchCount();

        return PageableExecutionUtils.getPage(vehicleList, pageable, () -> totalCount);
    }


    @Override
    public GetAdminVehicleResponse findVehicleAndNotebyId(Long id) {
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

        return GetAdminVehicleResponse.builder()
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

    public String localDateTimeToString(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return null;
        else
            return localDateTime.toLocalDate().toString();
    }

    private VehicleState toVehicleState(String word) {
        try {
            return VehicleState.valueOf(word.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ResponseCode.VEHICLE_STATE_NOT_FOUND);
        }
    }
}