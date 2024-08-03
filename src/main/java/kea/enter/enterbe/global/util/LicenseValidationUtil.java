package kea.enter.enterbe.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.codef.api.EasyCodef;
import io.codef.api.EasyCodefServiceType;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import kea.enter.enterbe.api.member.service.dto.request.LicenseValidationRequestDto;
import kea.enter.enterbe.api.member.service.dto.response.LicenseValidationResponseDto;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class LicenseValidationUtil {

    @Value("${license.client.id}")
    private String clientId;
    @Value("${license.client.secret}")
    private String clientSecret;
    @Value("${license.public.key}")
    private String publicKey;
    @Value("${license.url.demo}")
    private String demoUrl;
    @Value("${spring.profiles.active}")
    private String profile;


    // 면허 진위여부 api(외부 api) 호출 메서드
    public LicenseValidationResponseDto checkValidationLicense(
        LicenseValidationRequestDto licenseDto)
    {

        // #1. 코드에프 객체 생성 및 클라이언트 정보 설정 -> autowired가 안돼서 메서드에서 생성
        EasyCodef codef = new EasyCodef();
        codef.setClientInfoForDemo(clientId, clientSecret);
        codef.setPublicKey(publicKey);

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

        String productUrl = demoUrl;
        String result = null;

        try {
            result = codef.requestProduct(productUrl, EasyCodefServiceType.DEMO, parameterMap);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            throw new CustomException(ResponseCode.BAD_REQUEST);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            /* Sonarlint suggestion : Clean up whatever needs to be handled before interrupting  */
            Thread.currentThread().interrupt();
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);

        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new CustomException(ResponseCode.INTERNAL_SERVER_ERROR);
        }

        //	#4.코드에프 정보 결과 확인
        log.info("codefapi result: {}", result);

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


}
