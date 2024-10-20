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

    public SpaceMarineDto update(UpdateSpaceMarineRequest request) {
        var spaceMarine = spaceMarineMapper.toEntity(request);
        var saved = spaceMarineRepository.save(spaceMarine);
        return spaceMarineMapper.toDto(saved);
    }

    public void deleteById(Long id) {
        spaceMarineRepository.deleteById(id);
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
