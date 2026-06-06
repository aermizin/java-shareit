package ru.practicum.shareIt.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareIt.booking.dto.BookingResponseDto;
import ru.practicum.shareIt.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM Booking b " +
            "WHERE b.booker.id = :userId AND b.item.id = :itemId " +
            "AND b.status = 'APPROVED' AND b.end < :now")
    boolean isUserBookedItem(@Param("userId") Long userId,
                             @Param("itemId") Long itemId,
                             @Param("now") LocalDateTime now);

    @Query("SELECT b.id AS id, b.start AS start, b.end AS end, " +
            "b.booker AS booker, b.item AS item, b.status AS status " +
            "FROM Booking b WHERE b.id = :bookingId")
    Optional<BookingResponseDto> findBookingDtoById(@Param("bookingId") Long bookingId);

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);
    List<Booking> findByItem_Owner_IdOrderByStartDesc(Long ownerId);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, String status);
    List<Booking> findByItem_Owner_IdAndStatusOrderByStartDesc(Long ownerId, String status);

    @Query(value = "SELECT DISTINCT ON (b.item_id) b.* " +
            "FROM bookings b " +
            "WHERE b.item_id IN (:itemIds) " +
            "AND b.end_date < :now " +
            "AND b.status ='APPROVED' " +
            "ORDER BY b.item_id, b.start_date DESC", nativeQuery = true)
    List<Booking> findLastBookingsForItems(@Param("itemIds") List<Long> itemIds, @Param("now") LocalDateTime now);

    @Query(value = "SELECT DISTINCT ON (b.item_id) b.* " +
            "FROM bookings b " +
            "WHERE b.item_id IN (:itemIds) " +
            "AND b.start_date > :now " +
            "AND b.status = 'APPROVED' " +
            "ORDER BY b.item_id, b.start_date ASC", nativeQuery = true)
    List<Booking> findNextBookingsForItems(@Param("itemIds") List<Long> itemIds, @Param("now") LocalDateTime now);
}
