package ru.practicum.shareit.user.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserResponseDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Test
    void findAll_shouldReturnAllUsers() {
        User user1 = new User();
        user1.setName("Иван");
        user1.setEmail("ivan@mail.ru");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("Мария");
        user2.setEmail("maria@mail.ru");
        userRepository.save(user2);


        Collection<UserResponseDto> result = userService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(UserResponseDto::name)
                .containsExactlyInAnyOrder("Иван", "Мария");
        assertThat(result).extracting(UserResponseDto::email)
                .containsExactlyInAnyOrder("ivan@mail.ru", "maria@mail.ru");
    }
}

