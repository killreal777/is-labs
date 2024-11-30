package itmo.is.service.security.authorization;

import itmo.is.model.domain.Chapter;
import itmo.is.repository.domain.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChapterSecurityService extends OwnerEntitySecurityService<Chapter, Long> {
    private final ChapterRepository chapterRepository;

    @Override
    protected Chapter findById(Long aLong) {
        return chapterRepository.findById(aLong).orElseThrow();
    }
}
