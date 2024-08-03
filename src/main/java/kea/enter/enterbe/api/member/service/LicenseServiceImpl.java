package kea.enter.enterbe.api.member.service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import kea.enter.enterbe.api.member.controller.dto.request.LicenseDto;
import kea.enter.enterbe.api.member.service.dto.request.LicenseValidationRequestDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import kea.enter.enterbe.global.util.LicenseValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO: 전체적인 리턴 값좀 다시 확인해봐야 할 것 같음. void를 그냥 쓰는 건 난 함수 관점에서 별로임.
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LicenseServiceImpl implements LicenseService {

    private final MemberRepository memberRepository;
    private final LicenseValidationUtil licenseValidationUtil;

    // 면허증 정보 저장 메서드
    @Override
    @Transactional
    public void saveLicenseInformation(LicenseDto licenseDto) {

        // 개인정보 활용 동의를 하지 않았다면 서비스를 사용할 수 없음.
        if(licenseDto.getIsAgreeTerms().equals(Boolean.FALSE)) {
            throw new CustomException(ResponseCode.BAD_REQUEST);
        }

        Member member = findMemberByMemberId(licenseDto.getMemberId());
        String licenseId = licenseDto.getLicenseId();
        // 면허 진위여부 api 호출
        licenseValidationUtil.checkValidationLicense(
            LicenseValidationRequestDto.of(
                member.getBirthDate(),
                licenseId.substring(0,2),
                licenseId.substring(2,4),
                licenseId.substring(4,10),
                licenseId.substring(10,12),
                licenseDto.getLicensePassword(),
                member.getName()
            )
        );
        // 멤버 엔티티 수정
        // resAuthenticity 가 "1"이 아닌 경우에는 api 호출 함수에서 exception 처리
        member.setLicenseInformation(
            licenseDto.getLicenseId(),
            licenseDto.getLicensePassword(),
            true,
            licenseDto.getIsAgreeTerms()
        );
    }

    // 면허증 정보가 유효한지 확인하는 메서드
    @Override
    public void getLicenseInformation(Long memberId) {
        Member member = findMemberByMemberId(memberId);
        String licenseId = member.getLicenseId();
        String licensePassword = member.getLicensePassword();
        Boolean isLicenseValid = member.getIsLicenseValid();
        Integer age = member.getAge();
        // 만 26살 미만이라면 Exception
        if(age<26){
            throw new CustomException(ResponseCode.AGE_NOT_ALLOWED);
        }
        // 신청 기간 아니면 확인 및 진위여부 api를 호출하지 않도록 함.
        else if(!isWithinAllowedTime()){
            throw new CustomException(ResponseCode.NOT_APPLY_PERIOD);
        }
        // license 데이터가 없으면 Exception
        else if(licenseId.length()!=12 || licensePassword.isEmpty()){
            throw new CustomException(ResponseCode.LICENSE_NOT_FOUND);
        }
        // isLicneseValid == false 면 Exception
        else if (isLicenseValid.equals(Boolean.FALSE)) {
            // license data는 있으나, 유효성 검사가 필요합니다. 뭐 그런 얘기
            throw new CustomException(ResponseCode.LICENSE_VALIDATION_FALSE);
        }

        /*
        1. 신청 제출 할 때 진위여부 api 호출
            외부 api에서 0 또는 2를 받게 되면 다시 면허 등록 페이지로 이동 -> ~~
        2. 라우팅 처리 여부 확인
        만약 신청서 제출할 때 아래 patchLicense~ 메서드가 달린 컨트롤러를 사용한다면
        isLicenseValid 값의 여부와 상관없이 사용할 수 있도록 if문의 조건에서 제외하면 된다.
         */
    }

    // 면허 데이터가 이미 존재하고 isLicenseValid = false 인 사용자들의 데이터 진위여부 확인 메서드
    // -> 프론트에서 만약 진위여부가 참이 아니라는 말이 있으면 이 메서드가 달린 컨트롤러를 호출해주면 된다.
    @Override
    @Transactional
    public void patchLicenseInformation(Long memberId) {
        Member member = findMemberByMemberId(memberId);
        String licenseId = member.getLicenseId();
        String licensePassword = member.getLicensePassword();
        Boolean isAgreeTerms = member.getIsAgreeTerms();

        // 데이터 검사
        if(licenseId.length()==12 && !licensePassword.isEmpty() && isAgreeTerms.equals(Boolean.TRUE)){
            // 면허 진위여부 api 호출
            licenseValidationUtil.checkValidationLicense(
                LicenseValidationRequestDto.of(
                    member.getBirthDate(),
                    licenseId.substring(0,2),
                    licenseId.substring(2,4),
                    licenseId.substring(4,10),
                    licenseId.substring(10,12),
                    member.getLicensePassword(),
                    member.getName()
                )
            );
            member.setIsLicenseValid(true);
        }
        // db에 기존 데이터가 존재하지 않거나 정해진 형식이 아니라면
        else {
            throw new CustomException(ResponseCode.LICENSE_NOT_FOUND);
        }
    }

    // 신청 기간인지 확인하는 메서드
    private boolean isWithinAllowedTime() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        LocalTime time = now.toLocalTime();

        if (dayOfWeek == DayOfWeek.MONDAY) {
            return !time.isBefore(LocalTime.of(9, 0));
        } else if (dayOfWeek == DayOfWeek.TUESDAY) {
            return !time.isAfter(LocalTime.of(12, 0));
        }
        return false;
    }

    // id 값으로 member 객체 반환
    private Member findMemberByMemberId(Long memberId) {
        return memberRepository.findByIdAndState(memberId, MemberState.ACTIVE)
            .orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
    }

}
