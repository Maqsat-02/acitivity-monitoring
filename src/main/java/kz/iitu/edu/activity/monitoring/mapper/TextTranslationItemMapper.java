package kz.iitu.edu.activity.monitoring.mapper;

import kz.iitu.edu.activity.monitoring.dto.textitem.response.TextTranslationItemDto;
import kz.iitu.edu.activity.monitoring.dto.textitem.response.TranslationItemDto;
import kz.iitu.edu.activity.monitoring.entity.TextItem;
import kz.iitu.edu.activity.monitoring.entity.TranslationItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TextTranslationItemMapper {
    TextTranslationItemMapper INSTANCE = Mappers.getMapper(TextTranslationItemMapper.class);

    @Mapping(source = "textItem.id", target = "id")
    @Mapping(source = "textItem.shownOrdinal", target = "ordinal")
    @Mapping(source = "textItem.text", target = "text")
    @Mapping(source = "translationItem.text", target = "translationText", defaultValue = "")
    TextTranslationItemDto entitiesToDto(TextItem textItem, TranslationItem translationItem);

    @Mapping(source = "translationItem.changeOrdinal", target = "historyOrdinal")
    @Mapping(source = "translationItem.text", target = "translationText")
    TranslationItemDto entityToDto(TranslationItem translationItem);
}
