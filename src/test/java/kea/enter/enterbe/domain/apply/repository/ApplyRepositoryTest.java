package kea.enter.enterbe.domain.apply.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDate;
import java.util.List;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.lottery.controller.dto.request.ApplicantSearchType;
import kea.enter.enterbe.domain.apply.entity.Apply;
import kea.enter.enterbe.domain.apply.entity.ApplyPurpose;
import kea.enter.enterbe.domain.apply.entity.ApplyRound;
import kea.enter.enterbe.domain.apply.entity.ApplyRoundState;
import kea.enter.enterbe.domain.apply.entity.ApplyState;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.vehicle.entity.Vehicle;
import kea.enter.enterbe.domain.vehicle.entity.VehicleFuel;
import kea.enter.enterbe.domain.vehicle.entity.VehicleState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class ApplyRepositoryTest extends IntegrationTestSupport {
    @DisplayName(value = "해당 신청 회차의 신청 목록을 조회한다.")
    @Test
    public void findAllByApplyRoundIdAndState() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());

        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = createApply(member1, applyRound);
        Apply apply2 = createApply(member2, applyRound);
        applyRepository.saveAll(List.of(apply1, apply2));

        // when
        List<Apply> applyList = applyRepository.findAllByApplyRoundIdAndState(applyRound.getId(), ApplyState.ACTIVE);

        // then
        assertThat(applyList).hasSize(2)
            .extracting("id", "state")
            .containsExactlyInAnyOrder(
                tuple(apply1.getId(), ApplyState.ACTIVE),
                tuple(apply2.getId(), ApplyState.ACTIVE)
            );
    }

    @DisplayName(value = "해당 신청 회차의 신청 목록을 조회할 때 신청 회차가 다른 경우 조회되지 않는다.")
    @Test
    public void findAllByApplyRoundIdAndStateWithOtherApplyRound() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());

        ApplyRound applyRound1 = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));
        ApplyRound applyRound2 = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));


        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());

        Apply apply1 = createApply(member1, applyRound1);
        Apply apply2 = createApply(member2, applyRound1);
        applyRepository.saveAll(List.of(apply1, apply2));

        // when
        List<Apply> applyList = applyRepository.findAllByApplyRoundIdAndState(applyRound2.getId(), ApplyState.ACTIVE);

        // then
        assertThat(applyList).isEmpty();
    }

    @DisplayName("신청 내역 목록을 생성 시간 순으로 조회한다. (검색, 페이징)")
    @Test
    void findAllApplyByCondition() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));

        Member member1 = memberRepository.save(createMember());
        Member member2 = memberRepository.save(createMember());
        Member member3 = memberRepository.save(createMember());

        Apply apply1 = applyRepository.save(createApply(member1, applyRound));
        Apply apply2 = applyRepository.save(createApply(member2, applyRound));
        Apply apply3 = applyRepository.save(createApply(member3, applyRound));

        // when
        Page<Apply> applyList = applyRepository.findAllApplyByCondition(applyRound.getId(), null, ApplicantSearchType.ALL, PageRequest.of(0, 10));

        // then
        assertThat(applyList).hasSize(3)
            .extracting("id")
            .contains(apply3.getId(), apply2.getId(), apply1.getId());
    }

    @DisplayName("신청 내역 아이디 검색 시 키워드에 맞는 신청 내역만 조회한다.")
    @Test
    void findAllApplyByConditionWithIdKeyword() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));

        Member member1 = memberRepository.save(createMember("name", "test"));
        Member member2 = memberRepository.save(createMember("name", "test12"));
        Member member3 = memberRepository.save(createMember("name", "toast12"));

        applyRepository.save(createApply(member1, applyRound));
        applyRepository.save(createApply(member2, applyRound));
        applyRepository.save(createApply(member3, applyRound));

        // when
        Page<Apply> applyList = applyRepository.findAllApplyByCondition(applyRound.getId(), "test", ApplicantSearchType.ID, PageRequest.of(0, 10));

        // then
        assertThat(applyList).hasSize(2)
            .extracting("member.email")
            .contains(member2.getEmail(), member1.getEmail());
    }

    @DisplayName("신청 내역 이름 검색 시 키워드에 맞는 신청 내역만 조회한다.")
    @Test
    void findAllApplyByConditionWithNameKeyword() {
        // given
        Vehicle vehicle = vehicleRepository.save(createVehicle());
        ApplyRound applyRound = applyRoundRepository.save(createApplyRound(vehicle, LocalDate.of(2024, 7, 29)));

        Member member1 = memberRepository.save(createMember("name", "email"));
        Member member2 = memberRepository.save(createMember("name2", "email"));
        Member member3 = memberRepository.save(createMember("number", "email"));

        applyRepository.save(createApply(member1, applyRound));
        applyRepository.save(createApply(member2, applyRound));
        applyRepository.save(createApply(member3, applyRound));

        // when
        Page<Apply> applyList = applyRepository.findAllApplyByCondition(applyRound.getId(), "name", ApplicantSearchType.NAME, PageRequest.of(0, 10));

        // then
        assertThat(applyList).hasSize(2)
            .extracting("member.name")
            .contains(member2.getName(), member1.getName());
    }

    private Vehicle createVehicle() {
        return Vehicle.of("vehicleNo", "company", "model", 4,
            VehicleFuel.DIESEL, "img", VehicleState.AVAILABLE);
    }

    private ApplyRound createApplyRound(Vehicle vehicle, LocalDate takeDate) {
        return ApplyRound.of(vehicle, 1, takeDate, takeDate.plusDays(1), ApplyRoundState.ACTIVE);
    }

    private Member createMember(String name, String email) {
        return Member.of(name, email, "password", LocalDate.of(1999,11,28),
            "licenseId", "licensePassword", true, true,
            1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Member createMember() {
        return Member.of("name", "email", "password", LocalDate.of(1999,11,28),
            "licenseId", "licensePassword", true, true,
            1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Apply createApply(Member member, ApplyRound applyRound) {
        return Apply.of(member, applyRound, ApplyPurpose.EVENT, ApplyState.ACTIVE);
    }
}