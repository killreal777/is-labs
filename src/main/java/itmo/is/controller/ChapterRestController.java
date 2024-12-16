package itmo.is.controller;

import itmo.is.dto.domain.ChapterDto;
import itmo.is.dto.domain.request.CreateChapterRequest;
import itmo.is.dto.domain.request.UpdateChapterRequest;
import itmo.is.dto.history.ImportDto;
import itmo.is.model.security.Role;
import itmo.is.model.security.User;
import itmo.is.service.domain.ChapterService;
import itmo.is.service.history.ChapterImportHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/chapters")
@RequiredArgsConstructor
public class ChapterRestController {
    private final ChapterService chapterService;
    private final ChapterImportHistoryService chapterImportHistoryService;

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

    @PostMapping(value = "/imports", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ImportDto> importFile(@RequestPart("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(chapterService.importFile(file));
    }

    @GetMapping("/imports")
    public ResponseEntity<Page<ImportDto>> findAllImports(
            @AuthenticationPrincipal User user,
            @PageableDefault Pageable pageable
    ) {
        Page<ImportDto> result;
        if (user.getRole() == Role.ROLE_ADMIN) {
            result = chapterImportHistoryService.findAllImportLogs(pageable);
        } else {
            result = chapterImportHistoryService.findAllImportLogsByUser(user, pageable);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/imports/{id}/file")
    public ResponseEntity<ByteArrayResource> getFileByImportId(@PathVariable Long id) {
        ByteArrayResource file = chapterService.getImportFileByImportId(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename("chapter-import-" + id + ".json")
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(file);
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
        return ResponseEntity.noContent().build();
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
