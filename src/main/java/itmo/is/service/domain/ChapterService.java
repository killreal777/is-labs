package itmo.is.service.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.is.dto.domain.ChapterDto;
import itmo.is.dto.domain.request.CreateChapterRequest;
import itmo.is.dto.domain.request.UpdateChapterRequest;
import itmo.is.exception.EntityNotFoundWithIdException;
import itmo.is.exception.UniqueConstraintViolationException;
import itmo.is.mapper.domain.ChapterMapper;
import itmo.is.model.domain.Chapter;
import itmo.is.model.history.ChapterImport;
import itmo.is.repository.domain.ChapterRepository;
import itmo.is.service.history.ChapterImportHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterRepository chapterRepository;
    private final ChapterMapper chapterMapper;
    private final ChapterImportHistoryService chapterImportHistoryService;

    @Transactional
    public Page<ChapterDto> findAllWithFilters(String name, String parentLegion, Pageable pageable) {
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

    @Transactional
    public ChapterDto findById(Long id) {
        return chapterRepository.findById(id)
                .map(chapterMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Chapter.class, id));
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ChapterDto create(CreateChapterRequest request) {
        var chapter = chapterMapper.toEntity(request);
        validateUniqueChapterNameConstraint(chapter);
        var saved = chapterRepository.save(chapter);
        return chapterMapper.toDto(saved);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void importFile(MultipartFile file) {
        importChaptersLogProxy(parseFile(file));
    }

    private List<CreateChapterRequest> parseFile(MultipartFile file) {
        try {
            return new ObjectMapper().readValue(file.getBytes(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON file format", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file", e);
        }
    }

    private void importChaptersLogProxy(List<CreateChapterRequest> requests) {
        ChapterImport importLog = chapterImportHistoryService.createStartedImportLog();
        importChapters(requests);
        importLog.setSuccess(true);
        importLog.setObjectsAdded(requests.size());
        chapterImportHistoryService.saveFinishedImportLog(importLog);
    }

    private void importChapters(List<CreateChapterRequest> requests) {
        List<Chapter> chapters = requests.stream().map(chapterMapper::toEntity).toList();
        validateUniqueChapterNameConstraint(chapters);
        chapterRepository.saveAll(chapters);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ChapterDto update(Long id, UpdateChapterRequest request) {
        var original = chapterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Chapter.class, id));
        var updated = chapterMapper.toEntity(request);
        if (!original.getName().equals(updated.getName())) {
            validateUniqueChapterNameConstraint(updated);
        }
        updated.setId(id);
        updated.setOwner(original.getOwner());
        updated.setAdminEditAllowed(original.isAdminEditAllowed());
        var saved = chapterRepository.save(updated);
        return chapterMapper.toDto(saved);
    }

    private void validateUniqueChapterNameConstraint(Chapter chapter) {
        if (chapterRepository.existsByName(chapter.getName())) {
            throw new UniqueConstraintViolationException(Chapter.class, "name", chapter.getName());
        }
    }

    private void validateUniqueChapterNameConstraint(List<Chapter> chapters) {
        Set<String> names = new HashSet<>();
        chapters.forEach(chapter -> {
            if (!names.add(chapter.getName())) {
                throw new UniqueConstraintViolationException(Chapter.class, "name", chapter.getName());
            }
        });
        chapterRepository.findFirstByNameIn(names).ifPresent((spaceMarine) -> {
            throw new UniqueConstraintViolationException(Chapter.class, "name", spaceMarine.getName());
        });
    }

    @Transactional
    public void delete(Long id) {
        chapterRepository.deleteById(id);
    }

    @Transactional
    public void allowAdminEditing(Long id) {
        setAdminEditAllowed(id, true);
    }

    @Transactional
    public void denyAdminEditing(Long id) {
        setAdminEditAllowed(id, false);
    }

    private void setAdminEditAllowed(Long id, boolean adminEditAllowed) {
        var chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Chapter.class, id));
        chapter.setAdminEditAllowed(adminEditAllowed);
        chapterRepository.save(chapter);
    }

    @Transactional
    public Page<ChapterDto> findAllByNameContaining(String substring, Pageable pageable) {
        return chapterRepository.findAllByNameContaining(substring, pageable).map(chapterMapper::toDto);
    }
}
