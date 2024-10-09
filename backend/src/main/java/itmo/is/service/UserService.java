package itmo.is.service;

import itmo.is.model.security.User;
import itmo.is.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
    }

    public boolean isUserExist(String username) {
        return userRepository.existsByUsername(username);
    }
}