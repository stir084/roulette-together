package com.stir.roulette.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;


@Data
public class PageDTO<T> {

    List<T> content;

    CustomPage customPage;

    public PageDTO(Page<T> page) {
        this.content = page.getContent();
        this.customPage = new CustomPage(page.getTotalElements(),
                page.getTotalPages(), page.getNumber(), page.getSize());
    }


}