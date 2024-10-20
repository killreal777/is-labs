package itmo.is.service.domain;

import itmo.is.dto.domain.ChapterDto;
import itmo.is.dto.domain.request.CreateChapterRequest;
import itmo.is.dto.domain.request.UpdateChapterRequest;
import itmo.is.mapper.ChapterMapper;
import itmo.is.repository.ChapterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;

    public Page<ChapterDto> findAll(Pageable pageable) {
        return chapterRepository.findAll(pageable).map(chapterMapper::toDto);
    }

    public ChapterDto findById(Long id) {
        return chapterMapper.toDto(chapterRepository.findById(id).orElseThrow());
    }

    public ChapterDto save(CreateChapterRequest request) {
        var spaceMarine = chapterMapper.toEntity(request);
        var saved = chapterRepository.save(spaceMarine);
        return chapterMapper.toDto(saved);
    }

    public ChapterDto update(UpdateChapterRequest request) {
        var spaceMarine = chapterMapper.toEntity(request);
        var saved = chapterRepository.save(spaceMarine);
        return chapterMapper.toDto(saved);
    }

    public void deleteById(Long id) {
        chapterRepository.deleteById(id);
    }
}
