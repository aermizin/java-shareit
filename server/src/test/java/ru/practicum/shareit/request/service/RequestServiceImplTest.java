package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.RequestWithItemsResponseDto;
import ru.practicum.shareit.request.repository.RequestRepository;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
public class RequestServiceImplTest {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void getOwnRequests_shouldReturnRequestsWithItems() {
        User requester = new User();
        requester.setName("Иван");
        requester.setEmail("ivan@mail.ru");
        userRepository.save(requester);

        Request request1 = new Request();
        request1.setDescription("Нужна дрель");
        request1.setRequestor(requester);
        request1.setCreated(LocalDateTime.now().minusDays(1));
        requestRepository.save(request1);

        Request request2 = new Request();
        request2.setDescription("Нужна отвёртка");
        request2.setRequestor(requester);
        request2.setCreated(LocalDateTime.now());
        requestRepository.save(request2);

        Item item1 = new Item();
        item1.setName("Дрель");
        item1.setDescription("Ударная");
        item1.setAvailable(true);
        item1.setOwner(requester);
        item1.setRequest(request1);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Дрель аккумуляторная");
        item2.setDescription("Мощная");
        item2.setAvailable(true);
        item2.setOwner(requester);
        item2.setRequest(request1);
        itemRepository.save(item2);

        Item item3 = new Item();
        item3.setName("Отвёртка");
        item3.setDescription("Крестовая");
        item3.setAvailable(true);
        item3.setOwner(requester);
        item3.setRequest(request2);
        itemRepository.save(item3);

        List<RequestWithItemsResponseDto> result = requestService.getOwnRequests(requester.getId());

        assertThat(result).hasSize(2);

        RequestWithItemsResponseDto dto1 = result.stream()
                .filter(r -> r.id().equals(request1.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(dto1.description()).isEqualTo("Нужна дрель");
        assertThat(dto1.items()).hasSize(2);
        List<String> names = dto1.items().stream()
                .map(item -> item.name())
                .collect(Collectors.toList());
        assertThat(names).containsExactlyInAnyOrder("Дрель", "Дрель аккумуляторная");

        // Проверяем второй запрос
        RequestWithItemsResponseDto dto2 = result.stream()
                .filter(r -> r.id().equals(request2.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(dto2.description()).isEqualTo("Нужна отвёртка");
        assertThat(dto2.items()).hasSize(1);
        assertThat(dto2.items().get(0).name()).isEqualTo("Отвёртка");
    }
}
