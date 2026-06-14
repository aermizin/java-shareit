package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @EntityGraph("Item.comments")
    List<Item> findItemsFullByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i JOIN FETCH i.owner WHERE i.request.id IN :requestIds")
    Collection<Item> findByRequestIdIn(@Param("requestIds") List<Long> requestIds);

    @EntityGraph("Item.comments")
    Optional<Item> findItemFullById(Long id);

    @Query("SELECT i FROM Item i WHERE i.available = true AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    Collection<Item> searchItemsByText(String text);

}