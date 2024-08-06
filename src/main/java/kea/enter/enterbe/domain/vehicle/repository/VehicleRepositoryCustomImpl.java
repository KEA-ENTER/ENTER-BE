package kea.enter.enterbe.domain.vehicle.repository;

import static kea.enter.enterbe.domain.vehicle.entity.QVehicle.vehicle;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
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
