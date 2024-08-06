package kea.enter.enterbe.api.member.service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import kea.enter.enterbe.api.member.controller.dto.request.LicenseDto;
import kea.enter.enterbe.api.member.service.dto.request.LicenseValidationRequestDto;
import kea.enter.enterbe.api.member.service.dto.response.LicenseValidationResponseDto;
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

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LicenseServiceImpl implements LicenseService {

    private final MemberRepository memberRepository;
    private final LicenseValidationUtil licenseValidationUtil;
    private final Clock clock;

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
        LicenseValidationResponseDto dto = licenseValidationUtil.checkValidationLicense(
            LicenseValidationRequestDto.of(
                "0001",
                member.getBirthDate(),
                licenseId.substring(0,2),
                licenseId.substring(2,4),
                licenseId.substring(4,10),
                licenseId.substring(10,12),
                licenseDto.getLicensePassword(),
                member.getName()
            )
        );
        if(dto == null) throw new CustomException(ResponseCode.BAD_REQUEST);
        if(dto.getResAuthenticity().equals("1")){
            // 멤버 엔티티 수정
            member.setLicenseInformation(
                licenseDto.getLicenseId(),
                licenseDto.getLicensePassword(),
                true,
                licenseDto.getIsAgreeTerms()
            );
        }
        // resAuthenticity 0: 면허 일련번호 틀림, 2: 암호 일련번호 틀림
        else {
            throw new CustomException(ResponseCode.LICENSE_AUTHENTICITY_INCORRECT);
        }
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
    }

    // 면허 데이터가 이미 존재하고 isLicenseValid = false 인 사용자들의 데이터 진위여부 확인 메서드
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
            LicenseValidationResponseDto dto = licenseValidationUtil.checkValidationLicense(
                LicenseValidationRequestDto.of(
                    "0001",
                    member.getBirthDate(),
                    licenseId.substring(0,2),
                    licenseId.substring(2,4),
                    licenseId.substring(4,10),
                    licenseId.substring(10,12),
                    member.getLicensePassword(),
                    member.getName()
                )
            );
            if(dto.getResAuthenticity().equals("1")){
                member.setIsLicenseValid(true);
            }
            // resAuthenticity 0: 면허 일련번호 틀림, 2: 암호 일련번호 틀림
            else {
                throw new CustomException(ResponseCode.LICENSE_AUTHENTICITY_INCORRECT);
            }

        }
        // db에 기존 데이터가 존재하지 않거나 정해진 형식이 아니라면
        else {
            throw new CustomException(ResponseCode.LICENSE_NOT_FOUND);
        }
    }

    // 신청 기간인지 확인하는 메서드
    private boolean isWithinAllowedTime() {
        LocalDateTime now = LocalDateTime.now(clock);
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
