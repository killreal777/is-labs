package itmo.is.controller;

import itmo.is.dto.domain.SpaceMarineDto;
import itmo.is.dto.domain.request.CreateSpaceMarineRequest;
import itmo.is.dto.domain.request.UpdateSpaceMarineRequest;
import itmo.is.service.domain.SpaceMarineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/space-marines")
@RequiredArgsConstructor
public class SpaceMarineRestController {
    private final SpaceMarineService spaceMarineService;

    @GetMapping
    public ResponseEntity<Page<SpaceMarineDto>> findAll(@PageableDefault(page = 1) Pageable pageable) {
        return ResponseEntity.ok(spaceMarineService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceMarineDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(spaceMarineService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SpaceMarineDto> save(@RequestBody CreateSpaceMarineRequest request) {
        return ResponseEntity.ok(spaceMarineService.save(request));
    }

    @PreAuthorize("@spaceMarineSecurityService.hasEditRights(#request.id)")
    @PutMapping
    public ResponseEntity<SpaceMarineDto> update(@RequestBody UpdateSpaceMarineRequest request) {
        return ResponseEntity.ok(spaceMarineService.update(request));
    }

    @PreAuthorize("@spaceMarineSecurityService.isOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        spaceMarineService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
