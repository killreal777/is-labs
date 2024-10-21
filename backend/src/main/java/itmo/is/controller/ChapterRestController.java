package itmo.is.controller;

import itmo.is.dto.domain.ChapterDto;
import itmo.is.dto.domain.request.CreateChapterRequest;
import itmo.is.dto.domain.request.UpdateChapterRequest;
import itmo.is.service.domain.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(chapterService.findAllWithFilters(name, parentLegion, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChapterDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(chapterService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ChapterDto> create(@RequestBody CreateChapterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chapterService.create(request));
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        chapterService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("@chapterSecurityService.isOwner(#id)")
    @PutMapping("/{id}/admin-editing/allow")
    public ResponseEntity<Void> allowAdminEditing(@PathVariable Long id) {
        chapterService.allowAdminEditing(id);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("@chapterSecurityService.isOwner(#id)")
    @PutMapping("/{id}/admin-editing/deny")
    public ResponseEntity<Void> denyAdminEditing(@PathVariable Long id) {
        chapterService.denyAdminEditing(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/name-containing")
    public ResponseEntity<Page<ChapterDto>> findAllByNameContaining(
            @RequestParam String substring,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(chapterService.findAllByNameContaining(substring, pageable));
    }
}
