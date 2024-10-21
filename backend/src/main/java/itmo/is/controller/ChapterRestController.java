package itmo.is.controller;

import itmo.is.dto.domain.ChapterDto;
import itmo.is.dto.domain.request.CreateChapterRequest;
import itmo.is.dto.domain.request.EnrollRequest;
import itmo.is.dto.domain.request.ExpelRequest;
import itmo.is.dto.domain.request.UpdateChapterRequest;
import itmo.is.service.domain.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/chapters")
@RequiredArgsConstructor
public class ChapterRestController {
    private final ChapterService chapterService;

    @GetMapping
    public ResponseEntity<Page<ChapterDto>> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String parentLegion,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(chapterService.findAll(name, parentLegion, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(chapterService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ChapterDto> save(@RequestBody CreateChapterRequest request) {
        return ResponseEntity.ok(chapterService.save(request));
    }

    @PreAuthorize("@chapterSecurityService.hasEditRights(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<ChapterDto> update(
            @PathVariable Long id,
            @RequestBody UpdateChapterRequest request
    ) {
        return ResponseEntity.ok(chapterService.update(id, request));
    }

    @PreAuthorize("@chapterSecurityService.isOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        chapterService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("@spaceMarineSecurityService.isOwner(#id)")
    @PutMapping("/{id}/admin-editing/allow")
    public ResponseEntity<Void> allowAdminEditing(@PathVariable Long id) {
        chapterService.allowAdminEditing(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("@spaceMarineSecurityService.isOwner(#id)")
    @PutMapping("/{id}/admin-editing/deny")
    public ResponseEntity<Void> denyAdminEditing(@PathVariable Long id) {
        chapterService.denyAdminEditing(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/name-containing")
    public ResponseEntity<Page<ChapterDto>> findAllByNameContaining(
            @RequestParam String substring,
            @PageableDefault(size = 10, page = 0) Pageable pageable
    ) {
        return ResponseEntity.ok(chapterService.findAllByNameContaining(substring, pageable));
    }

    @PreAuthorize("@spaceMarineSecurityService.hasEditRights(#request.spaceMarineId())")
    @PutMapping("/enroll")
    public ResponseEntity<Void> enroll(@RequestBody EnrollRequest request) {
        chapterService.enroll(request);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("@spaceMarineSecurityService.hasEditRights(#request.spaceMarineId())")
    @PutMapping("expel")
    public ResponseEntity<Void> expel(@RequestBody ExpelRequest request) {
        chapterService.expel(request);
        return ResponseEntity.ok().build();
    }
}
