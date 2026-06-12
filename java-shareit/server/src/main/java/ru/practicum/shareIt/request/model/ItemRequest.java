package ru.practicum.shareIt.request.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareIt.user.model.User;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_request")
@Data
@Builder
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
