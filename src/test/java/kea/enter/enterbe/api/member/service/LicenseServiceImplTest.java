package kea.enter.enterbe.api.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;
import kea.enter.enterbe.IntegrationTestSupport;
import kea.enter.enterbe.api.member.controller.dto.request.LicenseDto;
import kea.enter.enterbe.api.member.service.dto.request.LicenseValidationRequestDto;
import kea.enter.enterbe.api.member.service.dto.response.LicenseValidationResponseDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberRole;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class LicenseServiceImplTest extends IntegrationTestSupport {

    /**
     * 1. saveLicenseInformation(LicenseDto licensedto) 테스트
     */

    @DisplayName("면허정보를 저장합니다. (성공)")
    @Test
    void saveLicenseInformation() {
        //given
        String licenseId = "111111111111"; // 12자리 숫자
        String liccensePw = "123ABC"; // 영문 1개 이상, 숫자 1개 이상 총 5-6 길이
        Boolean isAgreeTerm = true;

        Member member = memberRepository.save(createNoDataMember());
        Long memberId = member.getId();
        LicenseDto dto = LicenseDto.toService(
            memberId, licenseId, liccensePw, isAgreeTerm
        );
        // 외부 api는 실패하지 않는다는 가정하에 진행
        given(licenseValidationUtil.checkValidationLicense(
            any(LicenseValidationRequestDto.class)
        )).willReturn(LicenseValidationResponseDto.of(member.getName(), member.getBirthDate(), licenseId, "1", "ok", "ok", "11"));

        //when
        licenseService.saveLicenseInformation(dto);

        //then
        Optional<Member> result = memberRepository.findByIdAndState(memberId, MemberState.ACTIVE);
        assertThat(result).isPresent();
        assertThat(result.get())
            .extracting("isLicenseValid").isEqualTo(Boolean.TRUE);
    }

    @DisplayName("면허정보를 저장합니다. (실패 : 틀린 암호 일련번호)")
    @Test
    void throwExceptionWhenWrongLicensePassword() {
        //given
        /*
        12자리 숫자와 같은 패턴이 맞지 않으면 기존에 컨트롤러에서 dto를 전달할 수가 없기 때문에 패턴은 맞다고 가정
        암호 일련번호가 틀린 경우에 어떻게 동작하는지 확인
         */
        String licenseId = "111111111111"; // 12자리 숫자
        String liccensePw = "123AAA"; // 영문 1개 이상, 숫자 1개 이상 총 5-6 길이
        Boolean isAgreeTerm = true;

        Member member = memberRepository.save(createNoDataMember());
        Long memberId = member.getId();
        LicenseDto dto = LicenseDto.toService(
            memberId, licenseId, liccensePw, isAgreeTerm
        );
        //면허 일련번호는 전산정보와 일치하고 암호 일련번호가 틀린 경우 resAuthenticity 는 "2"를 반환
        given(licenseValidationUtil.checkValidationLicense(
            any(LicenseValidationRequestDto.class)
        )).willReturn(LicenseValidationResponseDto.of(member.getName(), member.getBirthDate(), licenseId, "2", "ok", "ok", "11"));


        //when & then
        assertThatThrownBy(() -> licenseService.saveLicenseInformation(dto))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.LICENSE_AUTHENTICITY_INCORRECT);
    }

    @DisplayName("면허정보를 저장합니다. (실패 : 개인정보 처리 미동의)")
    @Test
    void throwExceptionWhenIsNotAgreeTerms(){
        //given
        String licenseId = "111111111111"; // 12자리 숫자
        String liccensePw = "123ABC"; // 영문 1개 이상, 숫자 1개 이상 총 5-6 길이
        Boolean isAgreeTerm = false;

        Member member = memberRepository.save(createNoDataMember());
        Long memberId = member.getId();
        LicenseDto dto = LicenseDto.toService(
            memberId, licenseId, liccensePw, isAgreeTerm
        );

        //when & then
        assertThatThrownBy(() -> licenseService.saveLicenseInformation(dto))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.BAD_REQUEST);

    }

    /**
     * 2. getLicenseInformation(Long memberId) 테스트
     */

    @DisplayName("사용자의 면허 정보가 유효한지 확인합니다. (성공)")
    @Test
    void getLicenseInformation() {
        //given
        Member member = memberRepository.save(createAllowedConditionMember());
        Long memberId = member.getId();
        // 월요일 11시로 고정
        given(clock.instant()).willReturn(Instant.parse("2024-08-05T11:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.systemDefault());

        //when & then
        assertThatCode(() -> licenseService.getLicenseInformation(memberId))
            .doesNotThrowAnyException();
    }

    @DisplayName("사용자의 면허 정보가 유효한지 확인합니다. (실패 : 만 26살 미만)")
    @Test
    void throwExceptionWhenAgeIsLessThan26() {
        //given
        Member member = memberRepository.save(createNoDataMember());
        Long memberId = member.getId();

        // when & then
        assertThatThrownBy(() -> licenseService.getLicenseInformation(memberId))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.AGE_NOT_ALLOWED);
    }

    @DisplayName("사용자의 면허 정보가 유효한지 확인합니다. (실패 : 신청 기간이 아닙니다.")
    @Test
    void throwExceptionWhenNotApplyTime() {
        //given
        Member member = memberRepository.save(createAllowedConditionMember());
        Long memberId = member.getId();
        // 신청기간 : 월요일 오전 9시~화요일 밤 12시
        // 수요일 오전 10시로 고정
        given(clock.instant()).willReturn(Instant.parse("2024-08-07T10:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.systemDefault());

        //when & then
        assertThatThrownBy(() -> licenseService.getLicenseInformation(memberId))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.NOT_APPLY_PERIOD);
    }

    @DisplayName("사용자의 면허 정보가 유효한지 확인합니다. (실패 : 면허 정보가 없음)")
    @Test
    void throwExceptionWhenNoLicenseInformation() {
        //given
        //license data가 없는 사용자
        Member member = memberRepository.save(createNotHaveLicenseMember());
        Long memberId = member.getId();
        // 월요일 11시로 고정
        given(clock.instant()).willReturn(Instant.parse("2024-08-05T11:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.systemDefault());

        //when & then
        assertThatThrownBy(() -> licenseService.getLicenseInformation(memberId))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.LICENSE_NOT_FOUND);
    }

    @DisplayName("사용자의 면허 정보가 유효한지 확인합니다. (실패 : 면허 진위여부 검사가 되지 않았음.)")
    @Test
    void throwExceptionWhenIsNotLicenseValid() {
        //given
        //isLicenseValid == false
        Member member = memberRepository.save(createValidationCheckNeededMember());
        Long memberId = member.getId();
        // 월요일 11시로 고정
        given(clock.instant()).willReturn(Instant.parse("2024-08-05T11:00:00Z"));
        given(clock.getZone()).willReturn(ZoneId.systemDefault());

        //when & then
        assertThatThrownBy(() -> licenseService.getLicenseInformation(memberId))
            .isInstanceOf(CustomException.class)
            .extracting("responseCode")
            .isEqualTo(ResponseCode.LICENSE_VALIDATION_FALSE);
    }

    /**
     * 3. patchLicenseInformation(Long memberId) 테스트
     * 특이사항 : saveLicenseInformation()과 로직이 유사하므로 비슷한 테스트는 제외
     */

    @DisplayName("면허 데이터가 이미 존재하는 사용자들의 면허 진위여부를 확인합니다. (성공)")
    @Test
    void patchLicenseInformation() {
        //given
        Member member = memberRepository.save(createAllowedConditionMember());
        Long memberId = member.getId();
        String licenseId = member.getLicenseId();
        given(licenseValidationUtil.checkValidationLicense(
            any(LicenseValidationRequestDto.class)
        )).willReturn(LicenseValidationResponseDto.of(member.getName(), member.getBirthDate(), licenseId, "1", "ok", "ok", "11"));

        //when
        licenseService.patchLicenseInformation(memberId);

        //then
        Optional<Member> result = memberRepository.findByIdAndState(memberId, MemberState.ACTIVE);
        assertThat(result).isPresent();
        assertThat(result.get())
            .extracting("isLicenseValid").isEqualTo(Boolean.TRUE);
    }

    // 유형별 멤버 객체 생성
    private Member createNoDataMember() {
        return Member.of("원우형", "email", "password", LocalDate.of(1999,11,28), "",
            "", false, false, 1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Member createAllowedConditionMember() {
        return Member.of("원우형", "email", "password", LocalDate.of(1995,11,28), "111111111111",
            "123abc", true, true, 1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Member createNotHaveLicenseMember() {
        return Member.of("원우형", "email", "password", LocalDate.of(1995,11,28), "",
            "", false, false, 1, MemberRole.USER, MemberState.ACTIVE);
    }

    private Member createValidationCheckNeededMember() {
        return Member.of("원우형", "email", "password", LocalDate.of(1995,11,28), "111111111111",
            "123abc", false, true, 1, MemberRole.USER, MemberState.ACTIVE);
    }
}