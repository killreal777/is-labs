package itmo.is.service.history;

import itmo.is.dto.history.ImportLogDto;
import itmo.is.mapper.history.ImportLogMapper;
import itmo.is.model.history.ImportLog;
import itmo.is.model.security.User;
import itmo.is.repository.history.ImportLogRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.function.Supplier;

@RequiredArgsConstructor
public abstract class ImportHistoryService<T extends ImportLog> {
    private final ImportLogRepository<T> importLogRepository;
    private final ImportLogMapper importLogMapper;
    private final Supplier<T> importLogConstructor;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public T createStartedImportLog() {
        return importLogRepository.save(importLogConstructor.get());
    }

    @Transactional
    public void saveFinishedImportLog(T importLog) {
        importLogRepository.save(importLog);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Page<ImportLogDto> findAll(Pageable pageable) {
        return importLogRepository.findAll(pageable).map(importLogMapper::toDto);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Page<ImportLogDto> findAllByUser(User user, Pageable pageable) {
        return importLogRepository.findAllByUser(user, pageable).map(importLogMapper::toDto);
    }
}
