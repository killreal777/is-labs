package itmo.is.service.domain;

import itmo.is.dto.domain.ChapterDto;
import itmo.is.dto.domain.request.CreateChapterRequest;
import itmo.is.dto.domain.request.UpdateChapterRequest;
import itmo.is.mapper.domain.ChapterMapper;
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

    public Page<ChapterDto> findAll(String name, String parentLegion, Pageable pageable) {
        if (name != null && parentLegion != null) {
            return chapterRepository.findAllByNameAndParentLegion(name, parentLegion, pageable).map(chapterMapper::toDto);
        }
        if (name != null) {
            return chapterRepository.findAllByName(name, pageable).map(chapterMapper::toDto);
        }
        if (parentLegion != null) {
            return chapterRepository.findAllByParentLegion(parentLegion, pageable).map(chapterMapper::toDto);
        }
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

    public ChapterDto update(Long id, UpdateChapterRequest request) {
        var spaceMarine = chapterMapper.toEntity(request);
        spaceMarine.setId(id);
        var saved = chapterRepository.save(spaceMarine);
        return chapterMapper.toDto(saved);
    }

    public void deleteById(Long id) {
        chapterRepository.deleteById(id);
    }

    public void allowAdminEditing(Long id) {
        var chapter = chapterRepository.findById(id).orElseThrow();
        chapter.setAdminEditAllowed(true);
        chapterRepository.save(chapter);
    }

    public void denyAdminEditing(Long id) {
        var chapter = chapterRepository.findById(id).orElseThrow();
        chapter.setAdminEditAllowed(false);
        chapterRepository.save(chapter);
    }

    public Page<ChapterDto> findAllByNameContaining(String substring, Pageable pageable) {
        return chapterRepository.findAllByNameContaining(substring, pageable).map(chapterMapper::toDto);
    }
}
