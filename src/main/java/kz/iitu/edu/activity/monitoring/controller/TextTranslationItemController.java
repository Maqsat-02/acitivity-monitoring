package kz.iitu.edu.activity.monitoring.controller;

import kz.iitu.edu.activity.monitoring.dto.activity.request.ActivityCreationReq;
import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.dto.textitem.request.TranslationItemCreationReq;
import kz.iitu.edu.activity.monitoring.dto.textitem.response.TextTranslationItemDto;
import kz.iitu.edu.activity.monitoring.dto.textitem.response.TranslationItemDto;
import kz.iitu.edu.activity.monitoring.service.TextTranslationItemService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/textItems")
@AllArgsConstructor
public class TextTranslationItemController {
    private final TextTranslationItemService service;

    @GetMapping("/activity/{activityId}")
    @PreAuthorize(value = "hasAnyRole('TRANSLATOR', 'CHIEF_EDITOR','PROJECT_MANAGER')")
    public List<TextTranslationItemDto> getAll(@PathVariable("activityId") Long activityId) {
        return service.getAll(activityId);
    }

    @PostMapping("/{textItemId}/translationItems")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize(value = "hasRole('TRANSLATOR')")
    public TranslationItemDto create(@PathVariable("textItemId") Long textItemId,
                                     @RequestBody TranslationItemCreationReq creationReq) {
        return service.create(textItemId, creationReq);
    }

    @GetMapping("/{textItemId}/translationItems")
    @PreAuthorize(value = "hasAnyRole('TRANSLATOR', 'CHIEF_EDITOR','PROJECT_MANAGER')")
    public List<TranslationItemDto> getTranslationItemHistory(@PathVariable("textItemId") Long textItemId) {
        return service.getTranslationItemHistory(textItemId);
    }
}
