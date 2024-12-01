package itmo.is.repository.history;

import itmo.is.model.history.ChapterImportLog;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterImportLogRepository extends ImportLogRepository<ChapterImportLog> {
}
