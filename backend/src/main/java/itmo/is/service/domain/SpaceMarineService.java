package itmo.is.service.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

    @Transactional
    public void createBulk(MultipartFile file) {
        createBulk(parseFile(file));
    }

    @Transactional
    public void createBulk(List<CreateSpaceMarineRequest> requests) {
        Set<String> names = new HashSet<>();
        List<SpaceMarine> spaceMarines = requests.stream()
                .filter(request -> {
                    if (!names.add(request.name())) {
                        throw new IllegalArgumentException("Duplicate name found among requests: " + request.name());
                    }
                    return true;
                })
                .map(spaceMarineMapper::toEntity)
                .toList();
        spaceMarineRepository.findFirstByNameIn(names).ifPresent(spaceMarine -> {
            throw new IllegalArgumentException("Duplicate name found in database: " + spaceMarine.getName());
        });
        spaceMarineRepository.saveAll(spaceMarines);
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

    public SpaceMarineDto update(Long id, UpdateSpaceMarineRequest request) {
        var original = spaceMarineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(SpaceMarine.class, id));
        var updated = spaceMarineMapper.toEntity(request);
        updated.setId(id);
        updated.setOwner(original.getOwner());
        updated.setAdminEditAllowed(original.isAdminEditAllowed());
        var saved = spaceMarineRepository.save(updated);
        return spaceMarineMapper.toDto(saved);
    }

    public void delete(Long id) {
        SpaceMarine spaceMarine = spaceMarineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(SpaceMarine.class, id));

        if (spaceMarine.getChapter() != null) {
            decrementMarinesCount(spaceMarine.getChapter());
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

        incrementMarinesCount(chapter);
        spaceMarine.setChapter(chapter);
        spaceMarineRepository.save(spaceMarine);
    }

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

    private void incrementMarinesCount(Chapter chapter) {
        chapter.incrementMarinesCount();
        chapterRepository.save(chapter);
    }

    private void decrementMarinesCount(Chapter chapter) {
        chapter.decrementMarinesCount();
        chapterRepository.save(chapter);
    }
}
