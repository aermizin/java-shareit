package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
public class BookingServiceImplTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    void getBookings_shouldReturnAllBookings_whenStatusAll() {
        User booker = new User();
        booker.setName("Иван");
        booker.setEmail("ivan@mail.ru");
        userRepository.save(booker);

        User owner = new User();
        owner.setName("Пётр");
        owner.setEmail("petr@mail.ru");
        userRepository.save(owner);

        Item item = new Item();
        item.setName("Дрель");
        item.setDescription("Ударная");
        item.setAvailable(true);
        item.setOwner(owner);
        itemRepository.save(item);

        Instant now = Instant.now();

        Booking waitingBooking = createBooking(item, booker, now.plusSeconds(100),
                now.plusSeconds(200), BookingStatus.WAITING);
        Booking approvedBooking = createBooking(item, booker, now.plusSeconds(300),
                now.plusSeconds(400), BookingStatus.APPROVED);
        Booking rejectedBooking = createBooking(item, booker, now.plusSeconds(500),
                now.plusSeconds(600), BookingStatus.REJECTED);
        Booking pastBooking = createBooking(item, booker, now.minusSeconds(600),
                now.minusSeconds(500), BookingStatus.APPROVED);
        Booking currentBooking = createBooking(item, booker, now.minusSeconds(200),
                now.plusSeconds(200), BookingStatus.APPROVED);

        List<BookingResponseDto> result = bookingService.getBookings(booker.getId(), "ALL");

        assertThat(result).hasSize(5);
        assertThat(result).extracting(BookingResponseDto::id)
                .containsExactlyInAnyOrder(
                        waitingBooking.getId(),
                        approvedBooking.getId(),
                        rejectedBooking.getId(),
                        pastBooking.getId(),
                        currentBooking.getId()
                );
    }

    private Booking createBooking(Item item, User booker, Instant start, Instant end, BookingStatus status) {
        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }
}
