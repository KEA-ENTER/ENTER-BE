package kea.enter.enterbe.api.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import java.io.UnsupportedEncodingException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import kea.enter.enterbe.api.member.controller.dto.request.LicenseDto;
import kea.enter.enterbe.api.member.service.dto.request.LicenseValidationRequestDto;
import kea.enter.enterbe.api.member.service.dto.response.LicenseValidationResponseDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.entity.MemberState;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// TODO: 전체적인 리턴 값좀 다시 확인해봐야 할 것 같음. void를 그냥 쓰는 건 난 함수 관점에서 별로임.
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LicenseServiceImpl implements LicenseService {

    private final MemberRepository memberRepository;
    private final Environment environment;

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
        checkValidationLicense(
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
        // 만 26살 미만은 사용할 수 없음.
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
         */
    }

    // 면허 데이터가 이미 존재하고 isLicenseValid = false 인 사용자들의 데이터 진위여부 확인 메서드
    // -> 프론트에서 만약 진위여부가 참이 아니라는 말이 있으면 이 메서드가 달린 컨트롤러를 호출해주면 된다.
    // TODO: 코드가 중복되는데 어떻게 해결해야 할까?
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
            checkValidationLicense(
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
            // member 객체 수정
            member.setLicenseInformation(
                member.getLicenseId(),
                member.getLicensePassword(),
                true,
                member.getIsAgreeTerms()
            );
        }
        // 데이터가 존재하지 않는다면
        else {
            throw new CustomException(ResponseCode.LICENSE_NOT_FOUND);
        }
    }


    // 면허 진위여부 api(외부 api) 호출 메서드
    private LicenseValidationResponseDto checkValidationLicense(
        LicenseValidationRequestDto licenseDto)
    {

        // #1. 코드에프 객체 생성 및 클라이언트 정보 설정
        EasyCodef codef = new EasyCodef();
        codef.setClientInfoForDemo(environment.getProperty("CLIENT_ID"), environment.getProperty("CLIENT_SECRET"));
        codef.setPublicKey(environment.getProperty("PUBLIC_KEY"));

        // #2. 요청 파라미터 설정
        HashMap<String, Object> parameterMap = new HashMap<>();

        parameterMap.put("organization", licenseDto.getOrganization()); // 기관코드 설정
        parameterMap.put("birthDate", licenseDto.getBirthDate()); // 생년월일
        parameterMap.put("licenseNo01", licenseDto.getLicenseNo01()); // 운전 면허번호01 (지역)
        parameterMap.put("licenseNo02", licenseDto.getLicenseNo02()); // 운전 면허번호02 (년도)
        parameterMap.put("licenseNo03", licenseDto.getLicenseNo03()); // 운전 면허번호03
        parameterMap.put("licenseNo04", licenseDto.getLicenseNo04()); // 운전 면허번호04
        parameterMap.put("serialNo", licenseDto.getSerialNo()); // 일련번호
        parameterMap.put("userName", licenseDto.getUserName()); // 사용자이름

        // #3.코드에프 정보 조회 요청 - 서비스타입(API:정식, DEMO:데모, SANDBOX:샌드박스)

        String productUrl = environment.getProperty("DEMO_URL");
        String result = null;

        try {
            result = codef.requestProduct(productUrl, EasyCodefServiceType.DEMO, parameterMap);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            throw new CustomException(ResponseCode.BAD_REQUEST);
        } catch (JsonProcessingException | InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        //	#4.코드에프 정보 결과 확인
        log.info("codefapi");
        log.info(result);

        HashMap<String, Object> responseMap = null;
        try {
            responseMap = new ObjectMapper().readValue(result, HashMap.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }
        HashMap<String, String> resultMap = (HashMap<String, String>)responseMap.get("result");
        HashMap<String, String> dataMap = (HashMap<String, String>)responseMap.get("data");

        if(resultMap.get("code").equals("CF-00000") && dataMap.get("resAuthenticity").equals("1")){
            return LicenseValidationResponseDto.of(
                dataMap.get("resUserNm"), dataMap.get("commBirthDate"),
                dataMap.get("resLicenseNumber"), dataMap.get("resAuthenticity"),
                dataMap.get("resAuthenticityDesc1"), dataMap.get("resAuthenticityDesc2"),
                dataMap.get("resSearchDateTime")
            );
        }
        // 요청에 실패하거나, 진위여부가 참이 아닐 때(resAuthenticity가 0또는 2일 때)
        else {
            throw new CustomException(ResponseCode.LICENSE_AUTHENTICITY_INCORRECT);
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
