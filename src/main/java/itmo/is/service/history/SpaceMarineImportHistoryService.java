package itmo.is.service.history;

import itmo.is.mapper.history.ImportLogMapper;
import itmo.is.model.history.SpaceMarineImport;
import itmo.is.repository.history.SpaceMarineImportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpaceMarineImportHistoryService extends ImportHistoryService<SpaceMarineImport> {

    @Autowired
    public SpaceMarineImportHistoryService(
            SpaceMarineImportRepository importLogRepository,
            ImportLogMapper importLogMapper
    ) {
        super(importLogRepository, importLogMapper, SpaceMarineImport::new);
    }
}
