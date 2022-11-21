package com.stir.roulette.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;


@Data
@AllArgsConstructor
public class CustomPage {

    Long totalElements;

    int totalPages;

    int number;

    int size;
}