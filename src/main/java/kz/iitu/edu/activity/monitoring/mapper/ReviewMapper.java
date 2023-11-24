package kz.iitu.edu.activity.monitoring.mapper;

import kz.iitu.edu.activity.monitoring.dto.activity.response.ActivityDto;
import kz.iitu.edu.activity.monitoring.dto.review.response.ReviewDto;
import kz.iitu.edu.activity.monitoring.entity.FirebaseUser;
import kz.iitu.edu.activity.monitoring.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mapping(source = "review.id", target = "id")
    @Mapping(source = "review.activity.id", target = "activityId")
    @Mapping(source = "review.activity.title", target = "activityTitle")
    @Mapping(source = "chiefEditor", target = "chiefEditor")
    ReviewDto entitiesToDto(Review review, FirebaseUser chiefEditor);
}
