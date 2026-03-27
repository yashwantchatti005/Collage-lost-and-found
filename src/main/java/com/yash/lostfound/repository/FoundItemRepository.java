package com.yash.lostfound.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.yash.lostfound.model.FoundItem;

public interface FoundItemRepository extends JpaRepository<FoundItem, Long> {
}
  