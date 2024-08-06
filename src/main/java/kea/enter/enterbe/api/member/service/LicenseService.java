package kea.enter.enterbe.api.member.service;

import kea.enter.enterbe.api.member.controller.dto.request.LicenseDto;

public interface LicenseService {

    // 면허증 정보를 저장
    void saveLicenseInformation(LicenseDto licenseDto);
    // 면허증 정보가 유효한지 확인
    void getLicenseInformation(Long memberId);
    // 면허증 정보가 있는 사람의 진위여부 저장
    void patchLicenseInformation(Long memberId);
}
