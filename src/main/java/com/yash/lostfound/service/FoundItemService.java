package com.yash.lostfound.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yash.lostfound.model.FoundItem;
import com.yash.lostfound.repository.FoundItemRepository;

@Service
public class FoundItemService {

    @Autowired
    private FoundItemRepository repo;

    public void saveItem(FoundItem item) {
        item.setStatus("Available");
        repo.save(item);
    }

    public List<FoundItem> getAllItems() {
        return repo.findAll();
    }

    public void markClaimed(Long id) {
        FoundItem item = repo.findById(id).orElseThrow();
        item.setStatus("Claimed");
        repo.save(item);
    }
}
