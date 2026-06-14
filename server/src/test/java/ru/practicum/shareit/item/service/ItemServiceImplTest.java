package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemFullResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
public class ItemServiceImplTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void findAll_shouldReturnItemsWithBookings() {
        User owner = new User();
        owner.setName("Иван");
        owner.setEmail("ivan@mail.ru");
        userRepository.save(owner);

        Item item1 = new Item();
        item1.setName("Дрель");
        item1.setDescription("Ударная");
        item1.setAvailable(true);
        item1.setOwner(owner);
        itemRepository.save(item1);

        Item item2 = new Item();
        item2.setName("Молоток");
        item2.setDescription("Тяжёлый");
        item2.setAvailable(true);
        item2.setOwner(owner);
        itemRepository.save(item2);

        User booker = new User();
        booker.setName("Пётр");
        booker.setEmail("petr@mail.ru");
        userRepository.save(booker);

        Instant now = Instant.now();
        Booking pastBooking = new Booking();
        pastBooking.setStart(now.minusSeconds(7200));
        pastBooking.setEnd(now.minusSeconds(3600));
        pastBooking.setItem(item1);
        pastBooking.setBooker(booker);
        pastBooking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(pastBooking);

        Booking futureBooking = new Booking();
        futureBooking.setStart(now.plusSeconds(3600));
        futureBooking.setEnd(now.plusSeconds(7200));
        futureBooking.setItem(item1);
        futureBooking.setBooker(booker);
        futureBooking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(futureBooking);

        Booking pastBookingItem2 = new Booking();
        pastBookingItem2.setStart(now.minusSeconds(3600));
        pastBookingItem2.setEnd(now);
        pastBookingItem2.setItem(item2);
        pastBookingItem2.setBooker(booker);
        pastBookingItem2.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(pastBookingItem2);

        List<ItemFullResponseDto> result = itemService.findAll(owner.getId());


        assertThat(result).hasSize(2);

        ItemFullResponseDto dto1 = result.stream()
                .filter(i -> i.id().equals(item1.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(dto1.name()).isEqualTo("Дрель");
        assertThat(dto1.description()).isEqualTo("Ударная");
        assertThat(dto1.available()).isTrue();
        assertThat(dto1.lastBooking()).isEqualTo(pastBooking.getEnd());
        assertThat(dto1.nextBooking()).isEqualTo(futureBooking.getStart());

        ItemFullResponseDto dto2 = result.stream()
                .filter(i -> i.id().equals(item2.getId()))
                .findFirst()
                .orElseThrow();
        assertThat(dto2.name()).isEqualTo("Молоток");
        assertThat(dto2.description()).isEqualTo("Тяжёлый");
        assertThat(dto2.available()).isTrue();
        assertThat(dto2.lastBooking()).isEqualTo(pastBookingItem2.getEnd());
        assertThat(dto2.nextBooking()).isNull();
    }
}
