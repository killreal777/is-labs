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

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/space-marines")
@RequiredArgsConstructor
public class SpaceMarineRestController {
    private final SpaceMarineService spaceMarineService;

    @GetMapping
    public ResponseEntity<Page<SpaceMarineDto>> findAll(
            @RequestParam(required = false) String name,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(spaceMarineService.findAll(name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceMarineDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(spaceMarineService.findById(id));
    }

    @PostMapping
    public ResponseEntity<SpaceMarineDto> save(@RequestBody CreateSpaceMarineRequest request) {
        return ResponseEntity.ok(spaceMarineService.save(request));
    }

    @PreAuthorize("@spaceMarineSecurityService.hasEditRights(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<SpaceMarineDto> update(
            @PathVariable Long id,
            @RequestBody UpdateSpaceMarineRequest request
    ) {
        return ResponseEntity.ok(spaceMarineService.update(id, request));
    }

    @PreAuthorize("@spaceMarineSecurityService.isOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        spaceMarineService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("@spaceMarineSecurityService.isOwner(#id)")
    @PutMapping("/{id}/admin-editing/allow")
    public ResponseEntity<Void> allowAdminEditing(@PathVariable Long id) {
        spaceMarineService.allowAdminEditing(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("@spaceMarineSecurityService.isOwner(#id)")
    @PutMapping("/{id}/admin-editing/deny")
    public ResponseEntity<Void> denyAdminEditing(@PathVariable Long id) {
        spaceMarineService.denyAdminEditing(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/grouped-by-creation-date")
    public ResponseEntity<Map<LocalDate, Integer>> getGroupedByCreationDate() {
        return ResponseEntity.ok(spaceMarineService.getSpaceMarineCountByCreationDate());
    }

    @GetMapping("/name-containing")
    public ResponseEntity<Page<SpaceMarineDto>> findAllByNameContaining(
            @RequestParam String substring,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(spaceMarineService.findAllByNameContaining(substring, pageable));
    }

    @GetMapping("/loyal")
    public ResponseEntity<Page<SpaceMarineDto>> findAllLoyal(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(spaceMarineService.findAllLoyal(pageable));
    }

    @GetMapping("/disloyal")
    public ResponseEntity<Page<SpaceMarineDto>> findAllDisloyal(
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(spaceMarineService.findAllDisloyal(pageable));
    }
}
