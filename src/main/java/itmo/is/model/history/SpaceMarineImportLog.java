package itmo.is.model.history;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "marines_import")
public class SpaceMarineImportLog extends ImportLog {
}
