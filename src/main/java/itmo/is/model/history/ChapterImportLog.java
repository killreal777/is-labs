package itmo.is.model.history;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "chapters_import")
public class ChapterImportLog extends ImportLog {
}
