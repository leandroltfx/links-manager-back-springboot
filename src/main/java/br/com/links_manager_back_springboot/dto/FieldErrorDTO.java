package br.com.links_manager_back_springboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FieldErrorDTO {

    private String message;
    private String field;
}
