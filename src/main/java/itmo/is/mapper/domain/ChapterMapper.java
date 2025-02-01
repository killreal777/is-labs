package itmo.is.mapper.domain;

import itmo.is.dto.domain.ChapterDto;
import itmo.is.dto.domain.request.CreateChapterRequest;
import itmo.is.dto.domain.request.UpdateChapterRequest;
import itmo.is.mapper.EntityMapper;
import itmo.is.model.domain.Chapter;
import org.mapstruct.Mapper;

@Mapper
public interface ChapterMapper extends EntityMapper<ChapterDto, Chapter> {
    Chapter toEntity(CreateChapterRequest request);

    Chapter toEntity(UpdateChapterRequest request);
}
