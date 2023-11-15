package com.scl.devnest.checklist.controller;

import com.scl.devnest.checklist.service.ListItemService;
import com.scl.devnest.checklist.dto.ListItemDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/list-items")
public class ListItemController {

    @Autowired
    private ListItemService service;

    @GetMapping
    public ResponseEntity<List<ListItemDto>> getAll(
            @RequestParam(name = "checkListId", required = true) Long checkListId
    ) {
        List<ListItemDto> items = service.findByCheckListId(checkListId);
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody ListItemDto dto) throws Exception {
        ListItemDto result = service.add(dto);
        return ResponseEntity.created(URI.create("/list-items/" + result.getId())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody ListItemDto dto) throws Exception {
        dto.setId(id);
        ListItemDto result = service.update(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
