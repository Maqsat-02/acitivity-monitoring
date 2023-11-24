package kz.iitu.edu.activity.monitoring.controller;

import kz.iitu.edu.activity.monitoring.dto.remark.request.RemarkCreationReq;
import kz.iitu.edu.activity.monitoring.dto.remark.response.RemarkDto;
import kz.iitu.edu.activity.monitoring.service.RemarkService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class RemarkController {
    private final RemarkService remarkService;

    @PostMapping("/textItems/{textItemId}/latestTranslationItem/remarks")
    @PreAuthorize(value = "hasRole('CHIEF_EDITOR')")
    public RemarkDto createRemark(@PathVariable Long textItemId, @RequestBody RemarkCreationReq creationReq) {
        return remarkService.createRemark(textItemId, creationReq);
    }

    @GetMapping("/textItems/{textItemId}/latestTranslationItem/remarks")
    @PreAuthorize(value = "hasAnyRole('TRANSLATOR', 'CHIEF_EDITOR','PROJECT_MANAGER')")
    public List<RemarkDto> getRemarks(@PathVariable Long textItemId) {
        return remarkService.getRemarks(textItemId);
    }
}
