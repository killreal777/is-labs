package itmo.is.service.domain;

import itmo.is.dto.domain.SpaceMarineDto;
import itmo.is.dto.domain.request.CreateSpaceMarineRequest;
import itmo.is.dto.domain.request.UpdateSpaceMarineRequest;
import itmo.is.mapper.SpaceMarineMapper;
import itmo.is.repository.SpaceMarineRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SpaceMarineService {
    private final SpaceMarineRepository spaceMarineRepository;
    private final SpaceMarineMapper spaceMarineMapper;

    public Page<SpaceMarineDto> findAll(Pageable pageable) {
        return spaceMarineRepository.findAll(pageable).map(spaceMarineMapper::toDto);
    }

    public SpaceMarineDto findById(Long id) {
        return spaceMarineMapper.toDto(spaceMarineRepository.findById(id));
    }

    public SpaceMarineDto save(CreateSpaceMarineRequest request) {
        log.info("Save space marine service call: {}", request);
        var spaceMarine = spaceMarineMapper.toEntity(request);
        log.info("Mapped entity: {}", spaceMarine);
        var saved = spaceMarineRepository.save(spaceMarine);
        log.info("Saved entity: {}", spaceMarine);
        var dto = spaceMarineMapper.toDto(saved);
        log.info("Mapped DTO: {}", dto);
        return dto;
    }

    public SpaceMarineDto update(UpdateSpaceMarineRequest request) {
        var spaceMarine = spaceMarineMapper.toEntity(request);
        var saved = spaceMarineRepository.save(spaceMarine);
        return spaceMarineMapper.toDto(saved);
    }

    public void deleteById(Long id) {
        spaceMarineRepository.deleteById(id);
    }
}
