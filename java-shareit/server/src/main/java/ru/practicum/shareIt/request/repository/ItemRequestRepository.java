package ru.practicum.shareIt.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareIt.request.model.ItemRequest;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
}
