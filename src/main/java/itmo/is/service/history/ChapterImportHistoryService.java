package itmo.is.service.history;

import itmo.is.mapper.history.ImportMapper;
import itmo.is.model.history.ChapterImport;
import itmo.is.repository.history.ChapterImportRepository;
import itmo.is.service.aws.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterImportHistoryService extends ImportHistoryService<ChapterImport> {

    private static final String STORAGE_DIRECTORY_NAME = "chapters";

    @Autowired
    public ChapterImportHistoryService(
            ChapterImportRepository importLogRepository,
            ImportMapper importMapper,
            S3Service s3Service
    ) {
        super(
                importLogRepository,
                importMapper,
                ChapterImport::new,
                s3Service,
                STORAGE_DIRECTORY_NAME
        );
    }
}
