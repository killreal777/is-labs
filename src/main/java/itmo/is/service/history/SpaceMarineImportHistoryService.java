package itmo.is.service.history;

import itmo.is.mapper.history.ImportLogMapper;
import itmo.is.model.history.SpaceMarineImportLog;
import itmo.is.repository.history.SpaceMarineImportLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpaceMarineImportHistoryService extends ImportHistoryService<SpaceMarineImportLog> {

    @Autowired
    public SpaceMarineImportHistoryService(
            SpaceMarineImportLogRepository importLogRepository,
            ImportLogMapper importLogMapper
    ) {
        super(importLogRepository, importLogMapper, SpaceMarineImportLog::new);
    }
}
