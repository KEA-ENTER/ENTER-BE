package kea.enter.enterbe.domain.vehicle.repository;

import static kea.enter.enterbe.domain.vehicle.entity.QVehicle.vehicle;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import java.util.List;


public class VehicleRepositoryCumstomImpl extends QuerydslRepositorySupport implements VehicleRepositoryCustom {
    @Autowired
    private JPAQueryFactory queryFactory;

    public VehicleRepositoryCumstomImpl() {
        super(Vehicle.class);
    }

    @Override
    public Page<Vehicle> findBySearchOption(Pageable pageable, String vehicleNo, String model, VehicleState state) {
        JPQLQuery<Vehicle> query =  queryFactory.selectFrom(vehicle)
            .where(eqNo(vehicleNo), eqModel(model), eqState(state));

        List<Vehicle> vehicleList = this.getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<Vehicle>(vehicleList, pageable, query.fetchCount());
    }

    private BooleanExpression eqNo(String vehicleNo) {
        if(vehicleNo == null || vehicleNo.isEmpty()) {
            return null;
        }
        return vehicle.vehicleNo.eq(vehicleNo);
    }

    private BooleanExpression eqModel(String model) {
        if(model == null || model.isEmpty()) {
            return null;
        }
        return vehicle.model.eq(model);
    }

    private BooleanExpression eqState(VehicleState state) {
        if(state == null) {
            return null;
        }
        return vehicle.state.eq(state);
    }

}
