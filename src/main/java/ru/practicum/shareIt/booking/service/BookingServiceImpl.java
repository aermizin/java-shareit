package ru.practicum.shareIt.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareIt.booking.dto.BookingRequestDto;
import ru.practicum.shareIt.booking.dto.BookingResponseDto;
import ru.practicum.shareIt.booking.mapper.BookingMapper;
import ru.practicum.shareIt.booking.model.Booking;
import ru.practicum.shareIt.booking.model.BookingStatus;
import ru.practicum.shareIt.booking.repository.BookingRepository;
import ru.practicum.shareIt.exception.NotFoundException;
import ru.practicum.shareIt.exception.ValidationException;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.item.repository.ItemRepository;
import ru.practicum.shareIt.user.model.User;
import ru.practicum.shareIt.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponseDto getBooking(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с указанным ID не найдено"));

        Long bookerId = booking.getBooker().getId();
        Long ownerId = booking.getItem().getOwner().getId();

        if (!(bookerId.equals(userId) || ownerId.equals(userId))) {
            log.warn("Пользователь с id = {} не является владельцем или автором бронирования", userId);
            throw new ValidationException("Пользователь не является владельцем или автором бронирования");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookings(Long bookerId, String status) {
        userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));

        if (status.toLowerCase().equals("all")) {
            return bookingRepository.findByBookerIdOrderByStartDesc(bookerId).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        }

        return bookingRepository.findByBookerIdAndStatusOrderByStartDesc(bookerId, status).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getOwnerBookings(Long ownerId, String status) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));

        if (status.toLowerCase().equals("all")) {
            return bookingRepository.findByItem_Owner_IdOrderByStartDesc(ownerId).stream()
                    .map(BookingMapper::toBookingDto)
                    .collect(Collectors.toList());
        }

        return bookingRepository.findByItem_Owner_IdAndStatusOrderByStartDesc(ownerId, status).stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookingResponseDto create(Long bookerId, BookingRequestDto newBooking) {
        User booker = userRepository.findById(bookerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));

        Item item = itemRepository.findById(newBooking.getItemId())
                .orElseThrow(() -> new NotFoundException("Вещь с указанным ID не найдена"));

        if (!item.getAvailable()) {
            log.warn("Пользователь с id = {} попытался создать заявку на бронирование недоступной вещи с id = {}",
                    bookerId, newBooking.getItemId());
            throw new ValidationException("Вещь недоступна для бронирования");
        }

        Booking booking = BookingMapper.toBooking(newBooking, item, booker);
        Booking saved = bookingRepository.save(booking);
        log.info("Создана новая заявка на бронирование с id = {}", saved.getId());
        return BookingMapper.toBookingDto(saved);
    }

    @Override
    @Transactional
    public BookingResponseDto updateStatus(Long userId, Long bookingId, boolean status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с указанным ID не найдено"));

        User ownerItem = booking.getItem().getOwner();

        if (!ownerItem.getId().equals(userId)) {
            log.warn("Пользователь с id = {} попытался изменить статус бронирования с id = {}, не являясь владельцем вещи",
                    userId, booking.getId());
            throw new ValidationException("Управлять запросом на бронирование может только владелец");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            log.warn("Пользователь с id = {} попытался изменить статус {} бронирования с id = {} ",
                    userId, booking.getStatus(), booking.getId());
            throw new ValidationException("Бронирование должно иметь статус ожидания");
        }

        booking.setStatus(status ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking updated = bookingRepository.save(booking);
        log.info("Статус заявки обновлён: id = {}, новый статус = {}", updated.getId(), updated.getStatus());
        return BookingMapper.toBookingDto(updated);
    }
}


