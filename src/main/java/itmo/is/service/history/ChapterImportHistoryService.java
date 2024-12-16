package itmo.is.service.history;

import itmo.is.mapper.history.ImportLogMapper;
import itmo.is.model.history.ChapterImport;
import itmo.is.repository.history.ChapterImportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterImportHistoryService extends ImportHistoryService<ChapterImport> {

    @Autowired
    public ChapterImportHistoryService(
            ChapterImportRepository importLogRepository,
            ImportLogMapper importLogMapper
    ) {
        super(importLogRepository, importLogMapper, ChapterImport::new);
    }
}
