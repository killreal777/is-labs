package itmo.is.mapper;

import itmo.is.dto.domain.ChapterDto;
import itmo.is.model.domain.Chapter;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChapterMapper extends EntityMapper<ChapterDto, Chapter> {
}
