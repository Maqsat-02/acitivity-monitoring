package kz.iitu.edu.activity.monitoring.service;

import kz.iitu.edu.activity.monitoring.dto.review.response.ReviewDto;
import kz.iitu.edu.activity.monitoring.dto.textitem.request.TranslationItemCreationReq;
import kz.iitu.edu.activity.monitoring.dto.textitem.response.TextTranslationItemDto;
import kz.iitu.edu.activity.monitoring.dto.textitem.response.TranslationItemDto;
import kz.iitu.edu.activity.monitoring.entity.Activity;
import kz.iitu.edu.activity.monitoring.entity.Review;
import kz.iitu.edu.activity.monitoring.entity.TextItem;
import kz.iitu.edu.activity.monitoring.entity.TranslationItem;
import kz.iitu.edu.activity.monitoring.repository.TextItemRepository;
import kz.iitu.edu.activity.monitoring.repository.TranslationItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class TextTranslationItemServiceTest {
    @Mock
    TextItemRepository textItemRepository;
    @Mock
    TranslationItemRepository translationItemRepository;
    @Mock
    ActivityService activityService;
    @InjectMocks
    TextTranslationItemService textTranslationItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        TranslationItem expected = TranslationItem.builder().build();
        when(translationItemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        TranslationItem result = TranslationItem.builder().build();
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testCreate() {
        TranslationItem expected = TranslationItem.builder().build();
        when(translationItemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        TranslationItem result = TranslationItem.builder().build();
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetTranslationItemHistory() {
        TranslationItem expected = TranslationItem.builder().build();
        when(translationItemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        TranslationItem result = TranslationItem.builder().build();
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetByIdWithLatestTranslationItemOrThrow() {
        TranslationItem expected = TranslationItem.builder().build();
        when(translationItemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        TranslationItem result = TranslationItem.builder().build();
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetLatestTranslationItem() {
        TranslationItem expected = TranslationItem.builder().build();
        when(translationItemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(expected));
        TranslationItem result = TranslationItem.builder().build();
        Assertions.assertEquals(expected, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme