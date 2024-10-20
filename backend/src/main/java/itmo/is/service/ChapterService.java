package itmo.is.service;

import itmo.is.dto.domain.ChapterDto;
import itmo.is.dto.domain.request.CreateChapterRequest;
import itmo.is.dto.domain.request.UpdateChapterRequest;
import itmo.is.mapper.ChapterMapper;
import itmo.is.repository.ChapterRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;

    public List<ChapterDto> findAll() {
        return chapterMapper.toDto(chapterRepository.findAll());
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
