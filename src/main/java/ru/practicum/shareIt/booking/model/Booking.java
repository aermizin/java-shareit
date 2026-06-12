package ru.practicum.shareIt.booking.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareIt.item.model.Item;
import ru.practicum.shareIt.user.model.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@ToString(exclude = {"item", "booker"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return id != null && Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}
