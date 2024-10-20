package itmo.is.mapper;

import itmo.is.dto.domain.ChapterDto;
import itmo.is.dto.domain.request.CreateChapterRequest;
import itmo.is.dto.domain.request.UpdateChapterRequest;
import itmo.is.model.domain.Chapter;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChapterMapper extends EntityMapper<ChapterDto, Chapter> {
    Chapter toEntity(CreateChapterRequest request);

    Chapter toEntity(UpdateChapterRequest request);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<ChapterDto> toDto(List<Chapter> chapters);

    @IterableMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
    List<Chapter> toEntities(List<ChapterDto> chapterDtos);
}
