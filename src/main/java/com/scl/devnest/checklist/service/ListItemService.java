package com.scl.devnest.checklist.service;

import com.scl.devnest.checklist.dto.ListItemDto;
import com.scl.devnest.checklist.entity.CheckList;
import com.scl.devnest.checklist.entity.ListItem;
import com.scl.devnest.checklist.exception.EntityNotFoundException;
import com.scl.devnest.checklist.repository.CheckListRepository;
import com.scl.devnest.checklist.repository.ListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListItemService {

    @Autowired
    private ListItemRepository listItemRepository;

    @Autowired
    private CheckListRepository checkListRepository;

    public List<ListItemDto> findByCheckListId(Long checkListId) {
        return listItemRepository.findByCheckListId(checkListId).stream()
                .map(e -> this.convertToDto(e, false))
                .collect(Collectors.toList());
    }

    public ListItemDto add(ListItemDto dto) throws Exception {
        ListItem entity = convertToEntity(dto, true);
        entity = listItemRepository.save(entity);
        return convertToDto(entity, false);
    }

    public ListItemDto update(ListItemDto dto) throws Exception {
        ListItem entity = convertToEntity(dto, false);
        entity = listItemRepository.save(entity);
        return convertToDto(entity, false);
    }

    public void delete(Long id) throws Exception {
        ListItem entity = findEntity(id);
        listItemRepository.delete(entity);
    }

    private ListItemDto convertToDto(ListItem entity, boolean fetchAssociations) {
        ListItemDto dto = new ListItemDto();

        dto.setId(entity.getId());
        dto.setSummary(entity.getSummary());
        dto.setStatus(entity.getStatus());
        dto.setDueDate(entity.getDueDate());
        dto.setCheckListId(entity.getCheckList().getId());

        return dto;
    }

    private ListItem convertToEntity(ListItemDto dto, boolean isNew) throws Exception {
        ListItem entity = isNew? new ListItem() : findEntity(dto.getId());

        CheckList checkList = checkListRepository.findById(dto.getCheckListId())
                .orElseThrow(() -> new EntityNotFoundException("Could not find CheckList item with ID = " + dto.getCheckListId()));

        entity.setSummary(dto.getSummary());
        entity.setStatus(dto.getStatus());
        entity.setDueDate(dto.getDueDate());
        entity.setCheckList(checkList);

        return entity;
    }

    private ListItem findEntity(Long id) throws Exception {
        return listItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find a list item with id = " + id));
    }
}
