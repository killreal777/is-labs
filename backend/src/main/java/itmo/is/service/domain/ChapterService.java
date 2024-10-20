package itmo.is.service.domain;

import itmo.is.dto.domain.ChapterDto;
import itmo.is.dto.domain.request.CreateChapterRequest;
import itmo.is.dto.domain.request.EnrollRequest;
import itmo.is.dto.domain.request.ExpelRequest;
import itmo.is.dto.domain.request.UpdateChapterRequest;
import itmo.is.mapper.ChapterMapper;
import itmo.is.model.domain.Chapter;
import itmo.is.model.domain.SpaceMarine;
import itmo.is.repository.ChapterRepository;
import itmo.is.repository.SpaceMarineRepository;
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
    private final SpaceMarineRepository spaceMarineRepository;

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

    public ChapterDto update(UpdateChapterRequest request) {
        var spaceMarine = chapterMapper.toEntity(request);
        var saved = chapterRepository.save(spaceMarine);
        return chapterMapper.toDto(saved);
    }

    public void deleteById(Long id) {
        chapterRepository.deleteById(id);
    }

    public Page<ChapterDto> findAllByNameContaining(String substring, Pageable pageable) {
        return chapterRepository.findAllByNameContaining(substring, pageable).map(chapterMapper::toDto);
    }

    public void enroll(EnrollRequest request) {
        SpaceMarine spaceMarine = spaceMarineRepository.findById(request.spaceMarineId()).orElseThrow();
        if (spaceMarine.getChapter() != null) {
            throw new IllegalStateException("Space Marine already has a chapter");
        }
        Chapter chapter = chapterRepository.findById(request.chapterId()).orElseThrow();
        spaceMarine.setChapter(chapter);
        chapter.incrementMarinesCount();
        spaceMarineRepository.save(spaceMarine);
        chapterRepository.save(chapter);
    }

    public void expel(ExpelRequest request) {
        SpaceMarine spaceMarine = spaceMarineRepository.findById(request.spaceMarineId()).orElseThrow();
        if (spaceMarine.getChapter() == null) {
            throw new IllegalStateException("Space Marine does not have a chapter");
        }
        Chapter chapter = spaceMarine.getChapter();
        spaceMarine.setChapter(null);
        chapter.decrementMarinesCount();
        spaceMarineRepository.save(spaceMarine);
        chapterRepository.save(chapter);
    }
}
