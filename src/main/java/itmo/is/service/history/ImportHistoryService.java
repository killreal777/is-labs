package itmo.is.service.history;

import itmo.is.dto.history.ImportDto;
import itmo.is.exception.EntityNotFoundWithIdException;
import itmo.is.mapper.history.ImportMapper;
import itmo.is.model.history.Import;
import itmo.is.model.security.User;
import itmo.is.repository.history.ImportRepository;
import itmo.is.service.aws.S3Service;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class ImportHistoryService<T extends Import> {
    private final ImportRepository<T> importRepository;
    private final ImportMapper importMapper;
    private final Supplier<T> importLogConstructor;
    private final S3Service s3Service;
    private final String storageDirectoryName;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public T createStartedImportLog() {
        return importRepository.save(importLogConstructor.get());
    }

    @Transactional
    public ImportDto saveFinishedImportLog(T importLog) {
        return importMapper.toDto(importRepository.save(importLog));
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Page<ImportDto> findAllImportLogs(Pageable pageable) {
        return importRepository.findAll(pageable).map(importMapper::toDto);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Page<ImportDto> findAllImportLogsByUser(User user, Pageable pageable) {
        return importRepository.findAllByUser(user, pageable).map(importMapper::toDto);
    }

    public void saveImportFile(MultipartFile file, Import importLog) {
        String filename = filenameByImportId(importLog.getId());
        s3Service.save(filename, file);
    }

    public ByteArrayResource getFileByImportId(Long importId) {
        Import importLog = importRepository.findById(importId)
                .orElseThrow(() -> new EntityNotFoundWithIdException(Import.class, importId));
        if (!importLog.isSuccess()) {
            throw new IllegalArgumentException("Unsuccessful import");
        }
        String filename = filenameByImportId(importId);
        return s3Service.get(filename);
    }

    private String filenameByImportId(Long importId) {
        return String.format("%s/%d", storageDirectoryName, importId);
    }
}
