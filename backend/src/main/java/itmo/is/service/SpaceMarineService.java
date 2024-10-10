package itmo.is.service;

import itmo.is.dto.domain.SpaceMarineDto;
import itmo.is.dto.domain.request.CreateSpaceMarineRequest;
import itmo.is.mapper.SpaceMarineMapper;
import itmo.is.repository.SpaceMarineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpaceMarineService {
    private final SpaceMarineRepository spaceMarineRepository;
    private final SpaceMarineMapper spaceMarineMapper;

    public List<SpaceMarineDto> findAll() {
        return spaceMarineMapper.toDto(spaceMarineRepository.findAll());
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

    public void deleteById(Long id) {
        spaceMarineRepository.deleteById(id);
    }
}
