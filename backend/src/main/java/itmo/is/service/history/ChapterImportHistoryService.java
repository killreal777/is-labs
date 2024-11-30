package itmo.is.service.history;

import itmo.is.mapper.history.ImportLogMapper;
import itmo.is.model.history.ChapterImportLog;
import itmo.is.repository.history.ChapterImportLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterImportHistoryService extends ImportHistoryService<ChapterImportLog> {

    @Autowired
    public ChapterImportHistoryService(
            ChapterImportLogRepository importLogRepository,
            ImportLogMapper importLogMapper
    ) {
        super(importLogRepository, importLogMapper, ChapterImportLog::new);
    }
}
