package kea.enter.enterbe.api.apply.service;

import kea.enter.enterbe.api.apply.service.dto.DeleteApplyDetailServiceDto;

public interface ApplyServiceTestImpl {
    void deleteApplyDetailBefore(DeleteApplyDetailServiceDto dto);
    int deleteApplyDetailAfter(DeleteApplyDetailServiceDto dto);
}
