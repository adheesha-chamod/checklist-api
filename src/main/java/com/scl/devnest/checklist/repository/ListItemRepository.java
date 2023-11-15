package com.scl.devnest.checklist.repository;

import com.scl.devnest.checklist.entity.ListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ListItemRepository extends JpaRepository<ListItem, Long>, JpaSpecificationExecutor<ListItem> {

    public abstract List<ListItem> findByCheckListId(Long id);
}
