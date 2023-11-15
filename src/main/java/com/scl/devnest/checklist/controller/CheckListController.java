package com.scl.devnest.checklist.controller;

import com.scl.devnest.checklist.dto.CheckListDto;
import com.scl.devnest.checklist.enums.Status;
import com.scl.devnest.checklist.service.CheckListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/check-lists")
public class CheckListController {

    @Autowired
    private CheckListService service;

    @GetMapping
    public ResponseEntity<Page<CheckListDto>> search(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "description", required = false) String description,
            @RequestParam(name = "status", required = false) Status status,
            @RequestParam(name = "onlyActiveAllowed", required = false, defaultValue = "true") Boolean onlyActiveAllowed,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(service.getPage(name, description, status, onlyActiveAllowed, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckListDto> findById(@PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody CheckListDto dto) throws Exception {
        CheckListDto result = service.add(dto);
        URI uri = URI.create("/check-lists/" + result.getId());
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @RequestBody CheckListDto dto) throws Exception {
        dto.setId(id);
        CheckListDto result = service.update(dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) throws Exception {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
