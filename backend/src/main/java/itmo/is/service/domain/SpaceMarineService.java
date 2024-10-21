package itmo.is.service.domain;

import itmo.is.dto.domain.SpaceMarineDto;
import itmo.is.dto.domain.request.CreateSpaceMarineRequest;
import itmo.is.dto.domain.request.UpdateSpaceMarineRequest;
import itmo.is.mapper.domain.SpaceMarineMapper;
import itmo.is.model.domain.Chapter;
import itmo.is.model.domain.SpaceMarine;
import itmo.is.repository.ChapterRepository;
import itmo.is.repository.SpaceMarineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SpaceMarineService {
    private final SpaceMarineRepository spaceMarineRepository;
    private final SpaceMarineMapper spaceMarineMapper;
    private final ChapterRepository chapterRepository;

    public Page<SpaceMarineDto> findAll(String name, Pageable pageable) {
        if (name != null) {
            return spaceMarineRepository.findAllByName(name, pageable).map(spaceMarineMapper::toDto);
        }
        return spaceMarineRepository.findAll(pageable).map(spaceMarineMapper::toDto);
    }

    public SpaceMarineDto findById(Long id) {
        return spaceMarineMapper.toDto(spaceMarineRepository.findById(id).orElseThrow());
    }

    public SpaceMarineDto save(CreateSpaceMarineRequest request) {
        var spaceMarine = spaceMarineMapper.toEntity(request);
        var saved = spaceMarineRepository.save(spaceMarine);
        return spaceMarineMapper.toDto(saved);
    }

    public SpaceMarineDto update(Long id, UpdateSpaceMarineRequest request) {
        var spaceMarine = spaceMarineMapper.toEntity(request);
        spaceMarine.setId(id);
        var saved = spaceMarineRepository.save(spaceMarine);
        return spaceMarineMapper.toDto(saved);
    }

    public void deleteById(Long id) {
        spaceMarineRepository.deleteById(id);
    }

    public void allowAdminEditing(Long id) {
        var spaceMarine = spaceMarineRepository.findById(id).orElseThrow();
        spaceMarine.setAdminEditAllowed(true);
        spaceMarineRepository.save(spaceMarine);
    }

    public void denyAdminEditing(Long id) {
        var spaceMarine = spaceMarineRepository.findById(id).orElseThrow();
        spaceMarine.setAdminEditAllowed(false);
        spaceMarineRepository.save(spaceMarine);
    }

    public void enroll(Long spaceMarineId, Long chapterId) {
        SpaceMarine spaceMarine = spaceMarineRepository.findById(spaceMarineId).orElseThrow();
        if (spaceMarine.getChapter() != null) {
            throw new IllegalStateException("Space Marine already has a chapter");
        }
        Chapter chapter = chapterRepository.findById(chapterId).orElseThrow();
        spaceMarine.setChapter(chapter);
        chapter.incrementMarinesCount();
        spaceMarineRepository.save(spaceMarine);
        chapterRepository.save(chapter);
    }

    public void expel(Long spaceMarineId) {
        SpaceMarine spaceMarine = spaceMarineRepository.findById(spaceMarineId).orElseThrow();
        if (spaceMarine.getChapter() == null) {
            throw new IllegalStateException("Space Marine does not have a chapter");
        }
        Chapter chapter = spaceMarine.getChapter();
        spaceMarine.setChapter(null);
        chapter.decrementMarinesCount();
        spaceMarineRepository.save(spaceMarine);
        chapterRepository.save(chapter);
    }

    public Map<LocalDate, Integer> getSpaceMarineCountByCreationDate() {
        return spaceMarineRepository.countByCreationDateGrouped().stream()
                .collect(Collectors.toMap(
                        result -> (LocalDate) result[0],
                        result -> ((Long) result[1]).intValue()
                ));
    }

    public Page<SpaceMarineDto> findAllByNameContaining(String substring, Pageable pageable) {
        return spaceMarineRepository.findAllByNameContaining(substring, pageable).map(spaceMarineMapper::toDto);
    }

    public Page<SpaceMarineDto> findAllLoyal(Pageable pageable) {
        return spaceMarineRepository.findAllByLoyal(true, pageable).map(spaceMarineMapper::toDto);
    }

    public Page<SpaceMarineDto> findAllDisloyal(Pageable pageable) {
        return spaceMarineRepository.findAllByLoyal(false, pageable).map(spaceMarineMapper::toDto);
    }
}
