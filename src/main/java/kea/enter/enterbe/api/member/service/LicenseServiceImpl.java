package kea.enter.enterbe.api.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import kea.enter.enterbe.api.member.controller.dto.request.LicenseDto;
import kea.enter.enterbe.api.member.service.dto.request.LicenseValidationRequestDto;
import kea.enter.enterbe.api.member.service.dto.response.LicenseValidationResponseDto;
import kea.enter.enterbe.domain.member.entity.Member;
import kea.enter.enterbe.domain.member.repository.MemberRepository;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.UnsupportedEncodingException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LicenseServiceImpl implements LicenseService {

    private final MemberRepository memberRepository;
    private final Environment environment;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 면허증 정보 저장 메서드
    @Override
    public void saveLicenseInformation(LicenseDto licenseDto) {

        Member member = findMemberByMemberId(licenseDto.getMemberId());
        String licenseNum = licenseDto.getLicenseId();
        // 면허 진위여부 api 호출
        LicenseValidationResponseDto dto = checkValidationLicense(
            LicenseValidationRequestDto.of(
                member.getBirthDate().toString(),
                licenseNum.substring(0,2),
                licenseNum.substring(2,4),
                licenseNum.substring(4,10),
                licenseNum.substring(10,12),
                licenseDto.getLicensePassword(),
                member.getName()
            )
        );
        member.setLicenseInformation(
            dto.getResLicenseNumber(),
            licenseDto.getLicensePassword(),
            dto.getResAuthenticity(),
            licenseDto.getPrivateDataAgree()
        );
        memberRepository.save(member);
    }

    // 면허증 정보가 있는지 확인하는 메서드
    @Override
    public void getLicenseInformation(Long memberId) {
        // 신청 기간 아니면 확인 및 진위여부 api를 호출하지 않도록 함.
        if(!isWithinAllowedTime()){
            throw new CustomException(ResponseCode.METHOD_NOT_ALLOWED);
        }
        Member member = findMemberByMemberId(memberId);
        String licenseId = member.getLicenseId();
        boolean isLicenseValid = member.getIsLicenseValid();
        if(licenseId.length()!=12){
            throw new CustomException(ResponseCode.LICENSE_NOT_FOUND);
        }
        if (!isLicenseValid) {
            String licenseNum = member.getLicenseId();
            // 기존의 데이터로 진위여부 api 호출
            LicenseValidationResponseDto dto = checkValidationLicense(
                LicenseValidationRequestDto.of(
                    member.getBirthDate().toString(),
                    licenseNum.substring(0,2),
                    licenseNum.substring(2,4),
                    licenseNum.substring(4,10),
                    licenseNum.substring(10,12),
                    member.getLicensePassword(),
                    member.getName()
                    )
            );
            member.setLicenseInformation(
                dto.getResLicenseNumber(),
                member.getLicensePassword(),
                dto.getResAuthenticity(),
                member.getIsAgreeTerms()
            );
            memberRepository.save(member);
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
            throw new CustomException(ResponseCode.BAD_REQUEST);
        } catch (JsonProcessingException | InterruptedException e) {
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

//	#4.코드에프 정보 결과 확인
        logger.debug(result);

        HashMap<String, Object> responseMap = null;
        try {
            responseMap = new ObjectMapper().readValue(result, HashMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, String> resultMap = (HashMap<String, String>)responseMap.get("result");
        HashMap<String, String> dataMap = (HashMap<String, String>)responseMap.get("data");

        if(resultMap.get("code").equals("CF-00000") && dataMap.get("resAuthenticity").equals("1")){
            return LicenseValidationResponseDto.of(
                dataMap.get("resUserNm"), dataMap.get("commBirthDate"),
                dataMap.get("resLicenseNumber"), true,
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
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
    }
}
