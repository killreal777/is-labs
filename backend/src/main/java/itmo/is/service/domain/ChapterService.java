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
    private final ChapterRepository repository;
    private final ChapterMapper mapper;

    public Page<ChapterDto> findAll(String name, String parentLegion, Pageable pageable) {
        if (name != null && parentLegion != null) {
            return repository.findAllByNameAndParentLegion(name, parentLegion, pageable).map(mapper::toDto);
        }
        if (name != null) {
            return repository.findAllByName(name, pageable).map(mapper::toDto);
        }
        if (parentLegion != null) {
            return repository.findAllByParentLegion(parentLegion, pageable).map(mapper::toDto);
        }
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public ChapterDto findById(Long id) {
        return mapper.toDto(repository.findById(id).orElseThrow());
    }

    public ChapterDto save(CreateChapterRequest request) {
        var spaceMarine = mapper.toEntity(request);
        var saved = repository.save(spaceMarine);
        return mapper.toDto(saved);
    }

    public ChapterDto update(UpdateChapterRequest request) {
        var spaceMarine = mapper.toEntity(request);
        var saved = repository.save(spaceMarine);
        return mapper.toDto(saved);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Page<ChapterDto> findAllByNameContaining(String substring, Pageable pageable) {
        return repository.findAllByNameContaining(substring, pageable).map(mapper::toDto);
    }
}
