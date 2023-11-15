package com.scl.devnest.checklist.service;

import com.scl.devnest.checklist.dto.CheckListDto;
import com.scl.devnest.checklist.exception.EntityNotFoundException;
import com.scl.devnest.checklist.util.StringUtil;
import com.scl.devnest.checklist.entity.CheckList;
import com.scl.devnest.checklist.enums.Status;
import com.scl.devnest.checklist.repository.CheckListRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CheckListService {

    @Autowired
    private CheckListRepository repository;

    public Page<CheckListDto> getPage(String name, String description, Status status, Boolean onlyActiveAllowed, Pageable pageable) {
        Specification<CheckList> specification = getSpecification(name, description, status, onlyActiveAllowed);
        Page<CheckList> page = repository.findAll(specification, pageable);
        List<CheckListDto> checkLists = page.getContent().stream()
                .map(e -> this.convertToDto(e, false))
                .collect(Collectors.toList());

        return new PageImpl<>(checkLists, page.getPageable(), page.getTotalElements());
    }

    public CheckListDto findById(Long id) throws Exception {
        CheckList entity = findEntity(id);
        return entity != null? convertToDto(entity, true) : null;
    }

    public CheckListDto add(CheckListDto dto) throws Exception {
        CheckList entity = convertToEntity(dto, true);
        entity = repository.save(entity);
        return convertToDto(entity, false);
    }

    public CheckListDto update(CheckListDto dto) throws Exception {
        CheckList entity = convertToEntity(dto, false);
        entity = repository.save(entity);
        return convertToDto(entity, false);
    }

    public void delete(Long id) throws Exception {
        CheckList entity = findEntity(id);
        repository.delete(entity);
    }

    private CheckListDto convertToDto(CheckList entity, boolean fetchAssociations) {
        CheckListDto dto = new CheckListDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());

        return dto;
    }

    private CheckList convertToEntity(CheckListDto dto, boolean isNew) throws Exception {
        CheckList entity = isNew? new CheckList() : findEntity(dto.getId());

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setStatus(dto.getStatus());

        return entity;
    }

    private CheckList findEntity(Long id) throws Exception {
        CheckList entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find the Check List with Id = " + id));
        return entity;
    }

    private Specification<CheckList> getSpecification(String name, String description, Status status, Boolean onlyActiveAllowed) {
        Specification<CheckList> specification = new Specification<>() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
                Predicate predicate = builder.and();

                if(StringUtil.isNotEmpty(name)) {
                    predicate = builder.and(predicate, builder.like(builder.upper(root.get("name")), StringUtil.uppercaseAnyMatch(name)));
                }

                if(StringUtil.isNotEmpty(description)) {
                    predicate = builder.and(predicate, builder.like(builder.upper(root.get("description")), StringUtil.uppercaseAnyMatch(description)));
                }

                if(status != null) {
                    predicate = builder.and(predicate, builder.equal(root.get("status"), status));
                }

                if(Boolean.TRUE.equals(onlyActiveAllowed)) {
                    predicate = builder.and(predicate, builder.and(
                          builder.notEqual(root.get("status"), Status.DONE),
                          builder.notEqual(root.get("status"), Status.MARKED_DONE)
                    ));
                }

                return predicate;
            }
        };

        return specification;
    }
}
