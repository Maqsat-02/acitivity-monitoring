package kz.iitu.edu.activity.monitoring.mapper;

import kz.iitu.edu.activity.monitoring.dto.remark.response.RemarkDto;
import kz.iitu.edu.activity.monitoring.entity.Remark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RemarkMapper {
    RemarkMapper INSTANCE = Mappers.getMapper(RemarkMapper.class);

    @Mapping(source = "remark.review.id", target = "reviewId")
    RemarkDto entityToDto(Remark remark);
}
