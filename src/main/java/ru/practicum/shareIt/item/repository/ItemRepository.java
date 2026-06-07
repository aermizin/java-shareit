package ru.practicum.shareIt.item.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareIt.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @EntityGraph("Item.comments")
    List<Item> findItemsFullByOwnerId(Long ownerId);

    @EntityGraph("Item.comments")
    Optional<Item> findItemFullById(Long id);

    @Query("SELECT i FROM Item i WHERE i.available = true AND (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')))")
    Collection<Item> searchItemsByText(String text);
}
