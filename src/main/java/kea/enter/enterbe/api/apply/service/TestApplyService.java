package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.service.dto.DeleteApplyDetailServiceDto;

public interface TestApplyService {
    void deleteApplyDetailBefore(DeleteApplyDetailServiceDto dto);
    int deleteApplyDetailAfter(DeleteApplyDetailServiceDto dto);
}
