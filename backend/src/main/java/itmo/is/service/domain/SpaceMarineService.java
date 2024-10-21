package itmo.is.service.domain;

import itmo.is.dto.domain.SpaceMarineDto;
import itmo.is.dto.domain.request.CreateSpaceMarineRequest;
import itmo.is.dto.domain.request.UpdateSpaceMarineRequest;
import itmo.is.exception.EntityNotFoundWithIdException;
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

    public Page<SpaceMarineDto> findAllWithFilters(String name, Pageable pageable) {
        if (name != null) {
            return spaceMarineRepository.findAllByName(name, pageable).map(spaceMarineMapper::toDto);
        }
        return spaceMarineRepository.findAll(pageable).map(spaceMarineMapper::toDto);
    }

    public SpaceMarineDto findById(Long id) {
        return spaceMarineRepository.findById(id)
                .map(spaceMarineMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundWithIdException(SpaceMarine.class, id));
    }

    public SpaceMarineDto create(CreateSpaceMarineRequest request) {
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

    public void delete(Long id) {
        if (!spaceMarineRepository.existsById(id)) {
            throw new EntityNotFoundWithIdException(SpaceMarine.class, id);
        }
        spaceMarineRepository.deleteById(id);
    }

    public void allowAdminEditing(Long id) {
        setAdminEditAllowed(id, true);
    }

    public void denyAdminEditing(Long id) {
        setAdminEditAllowed(id, false);
    }

    private void setAdminEditAllowed(Long id, boolean adminEditAllowed) {
        var chapter = spaceMarineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(SpaceMarine.class, id));
        chapter.setAdminEditAllowed(adminEditAllowed);
        spaceMarineRepository.save(chapter);
    }

    public void enroll(Long spaceMarineId, Long chapterId) {
        SpaceMarine spaceMarine = spaceMarineRepository.findById(spaceMarineId)
                .orElseThrow(() -> new EntityNotFoundWithIdException(SpaceMarine.class, spaceMarineId));

        if (spaceMarine.getChapter() != null) {
            throw new IllegalStateException("Space Marine already assigned to a chapter.");
        }

        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Chapter.class, spaceMarineId));

        spaceMarine.setChapter(chapter);
        chapter.incrementMarinesCount();

        spaceMarineRepository.save(spaceMarine);
        chapterRepository.save(chapter);
    }

    public void expel(Long spaceMarineId) {
        SpaceMarine spaceMarine = spaceMarineRepository.findById(spaceMarineId)
                .orElseThrow(() -> new EntityNotFoundWithIdException(SpaceMarine.class, spaceMarineId));

        if (spaceMarine.getChapter() == null) {
            throw new IllegalStateException("Space Marine is not assigned to any chapter.");
        }

        Chapter chapter = spaceMarine.getChapter();
        spaceMarine.setChapter(null);
        chapter.decrementMarinesCount();

        spaceMarineRepository.save(spaceMarine);
        chapterRepository.save(chapter);
    }

    public Map<LocalDate, Integer> countByCreationDate() {
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
