package itmo.is.service.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.is.dto.domain.SpaceMarineDto;
import itmo.is.dto.domain.request.CreateSpaceMarineRequest;
import itmo.is.dto.domain.request.UpdateSpaceMarineRequest;
import itmo.is.dto.history.ImportDto;
import itmo.is.exception.EntityNotFoundWithIdException;
import itmo.is.exception.UniqueConstraintViolationException;
import itmo.is.mapper.domain.SpaceMarineMapper;
import itmo.is.model.domain.Chapter;
import itmo.is.model.domain.SpaceMarine;
import itmo.is.model.history.SpaceMarineImport;
import itmo.is.repository.domain.ChapterRepository;
import itmo.is.repository.domain.SpaceMarineRepository;
import itmo.is.service.history.SpaceMarineImportHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SpaceMarineService {
    private final SpaceMarineRepository spaceMarineRepository;
    private final SpaceMarineMapper spaceMarineMapper;
    private final ChapterRepository chapterRepository;
    private final SpaceMarineImportHistoryService spaceMarineImportHistoryService;

    @Transactional
    public Page<SpaceMarineDto> findAllWithFilters(String name, Pageable pageable) {
        if (name != null) {
            return spaceMarineRepository.findAllByName(name, pageable).map(spaceMarineMapper::toDto);
        }
        return spaceMarineRepository.findAll(pageable).map(spaceMarineMapper::toDto);
    }

    @Transactional
    public SpaceMarineDto findById(Long id) {
        return spaceMarineRepository.findById(id)
                .map(spaceMarineMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundWithIdException(SpaceMarine.class, id));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public SpaceMarineDto create(CreateSpaceMarineRequest request) {
        var spaceMarine = spaceMarineMapper.toEntity(request);
        validateUniqueSpaceMarineNameConstraint(spaceMarine); // ТЗ: реализовать ограничение уникальности в рамках бизнес-логики
        var saved = spaceMarineRepository.save(spaceMarine);
        return spaceMarineMapper.toDto(saved);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ImportDto importFile(MultipartFile file) {
        SpaceMarineImport importLog = spaceMarineImportHistoryService.createStartedImportLog();
        List<CreateSpaceMarineRequest> requests = parseFile(file);
        importSpaceMarines(requests);
        importLog.setSuccess(true);
        importLog.setObjectsAdded(requests.size());
        spaceMarineImportHistoryService.saveImportFile(file, importLog);
        return spaceMarineImportHistoryService.saveFinishedImportLog(importLog);
    }

    private List<CreateSpaceMarineRequest> parseFile(MultipartFile file) {
        try {
            return new ObjectMapper().readValue(file.getBytes(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON file format", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }

    private void importSpaceMarines(List<CreateSpaceMarineRequest> requests) {
        List<SpaceMarine> spaceMarines = requests.stream().map(spaceMarineMapper::toEntity).toList();
        validateUniqueSpaceMarineNameConstraint(spaceMarines); // ТЗ: реализовать ограничение уникальности в рамках бизнес-логики
        spaceMarineRepository.saveAll(spaceMarines);
    }

    public ByteArrayResource getImportFileByImportId(Long importId) {
        return spaceMarineImportHistoryService.getFileByImportId(importId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public SpaceMarineDto update(Long id, UpdateSpaceMarineRequest request) {
        var original = spaceMarineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(SpaceMarine.class, id));
        var updated = spaceMarineMapper.toEntity(request);
        if (!original.getName().equals(updated.getName())) {
            validateUniqueSpaceMarineNameConstraint(updated);
        }
        updated.setId(id);
        updated.setOwner(original.getOwner());
        updated.setAdminEditAllowed(original.isAdminEditAllowed());
        var saved = spaceMarineRepository.save(updated);
        return spaceMarineMapper.toDto(saved);
    }

    private void validateUniqueSpaceMarineNameConstraint(SpaceMarine spaceMarine) {
        if (spaceMarineRepository.existsByName(spaceMarine.getName())) {
            throw new UniqueConstraintViolationException(SpaceMarine.class, "name", spaceMarine.getName());
        }
    }

    private void validateUniqueSpaceMarineNameConstraint(List<SpaceMarine> spaceMarines) {
        Set<String> names = new HashSet<>();
        spaceMarines.forEach(chapter -> {
            if (!names.add(chapter.getName())) {
                throw new UniqueConstraintViolationException(SpaceMarine.class, "name", chapter.getName());
            }
        });
        spaceMarineRepository.findFirstByNameIn(names).ifPresent((spaceMarine) -> {
            throw new UniqueConstraintViolationException(SpaceMarine.class, "name", spaceMarine.getName());
        });
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void delete(Long id) {
        spaceMarineRepository.findById(id).ifPresent(spaceMarine -> {
            if (spaceMarine.getChapter() != null) {
                decrementMarinesCount(spaceMarine.getChapter());
            }
            spaceMarineRepository.deleteById(id);
        });
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void allowAdminEditing(Long id) {
        setAdminEditAllowed(id, true);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void denyAdminEditing(Long id) {
        setAdminEditAllowed(id, false);
    }

    private void setAdminEditAllowed(Long id, boolean adminEditAllowed) {
        var chapter = spaceMarineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(SpaceMarine.class, id));
        chapter.setAdminEditAllowed(adminEditAllowed);
        spaceMarineRepository.save(chapter);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void enroll(Long spaceMarineId, Long chapterId) {
        SpaceMarine spaceMarine = spaceMarineRepository.findById(spaceMarineId)
                .orElseThrow(() -> new EntityNotFoundWithIdException(SpaceMarine.class, spaceMarineId));

        if (spaceMarine.getChapter() != null) {
            throw new IllegalStateException("Space Marine already assigned to a chapter.");
        }

        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Chapter.class, spaceMarineId));

        incrementMarinesCount(chapter);
        spaceMarine.setChapter(chapter);
        spaceMarineRepository.save(spaceMarine);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void expel(Long spaceMarineId) {
        SpaceMarine spaceMarine = spaceMarineRepository.findById(spaceMarineId)
                .orElseThrow(() -> new EntityNotFoundWithIdException(SpaceMarine.class, spaceMarineId));

        if (spaceMarine.getChapter() == null) {
            throw new IllegalStateException("Space Marine is not assigned to any chapter.");
        }

        Chapter chapter = spaceMarine.getChapter();
        decrementMarinesCount(chapter);
        spaceMarine.setChapter(null);

        spaceMarineRepository.save(spaceMarine);
    }

    @Transactional
    public Map<LocalDate, Integer> countByCreationDate() {
        return spaceMarineRepository.countByCreationDateGrouped().stream()
                .collect(Collectors.toMap(
                        result -> (LocalDate) result[0],
                        result -> ((Long) result[1]).intValue()
                ));
    }

    @Transactional
    public Page<SpaceMarineDto> findAllByNameContaining(String substring, Pageable pageable) {
        return spaceMarineRepository.findAllByNameContaining(substring, pageable).map(spaceMarineMapper::toDto);
    }

    @Transactional
    public Page<SpaceMarineDto> findAllLoyal(Pageable pageable) {
        return spaceMarineRepository.findAllByLoyal(true, pageable).map(spaceMarineMapper::toDto);
    }

    @Transactional
    public Page<SpaceMarineDto> findAllDisloyal(Pageable pageable) {
        return spaceMarineRepository.findAllByLoyal(false, pageable).map(spaceMarineMapper::toDto);
    }

    private void incrementMarinesCount(Chapter chapter) {
        chapter.incrementMarinesCount();
        chapterRepository.save(chapter);
    }

    private void decrementMarinesCount(Chapter chapter) {
        chapter.decrementMarinesCount();
        chapterRepository.save(chapter);
    }
}
