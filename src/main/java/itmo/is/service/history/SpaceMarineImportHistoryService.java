package itmo.is.service.history;

import itmo.is.mapper.history.ImportMapper;
import itmo.is.model.history.SpaceMarineImport;
import itmo.is.repository.history.SpaceMarineImportRepository;
import itmo.is.service.aws.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpaceMarineImportHistoryService extends ImportHistoryService<SpaceMarineImport> {

    private static final String STORAGE_DIRECTORY_NAME = "marines";

    @Autowired
    public SpaceMarineImportHistoryService(
            SpaceMarineImportRepository importLogRepository,
            ImportMapper importMapper,
            S3Service s3Service
    ) {
        super(
                importLogRepository,
                importMapper,
                SpaceMarineImport::new,
                s3Service,
                STORAGE_DIRECTORY_NAME
        );
    }
}
