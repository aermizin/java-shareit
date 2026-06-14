package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.repository.projection.RequestShortProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r FROM Request r JOIN FETCH r.items WHERE r.id = :requestId")
    Optional<Request> findByIdWithItems(@Param("requestId") Long requestId);

    List<Request> findByRequestorIdOrderByCreatedDesc(long userId);

    List<RequestShortProjection> findByRequestor_IdNotOrderByCreatedDesc(long userId);
}
