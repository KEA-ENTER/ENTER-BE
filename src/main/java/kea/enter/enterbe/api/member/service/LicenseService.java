package kea.enter.enterbe.api.member.service;

import kea.enter.enterbe.api.member.controller.dto.request.LicenseDto;

public interface LicenseService {

    // license 정보를 저장
    void saveLicenseInformation(LicenseDto licenseDto);
    // license data가 있는지 확인
    void getLicenseInformation(Long memberId);

}
