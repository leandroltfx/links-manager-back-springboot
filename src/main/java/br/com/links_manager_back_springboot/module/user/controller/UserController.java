package br.com.links_manager_back_springboot.module.user.controller;

import br.com.links_manager_back_springboot.dto.ApiResponseDTO;
import br.com.links_manager_back_springboot.module.user.dto.CreateUserRequestDTO;
import br.com.links_manager_back_springboot.module.user.useCase.CreateUserUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO> createUser(
            @Valid @RequestBody CreateUserRequestDTO createUserRequestDTO
    ) {
        var result = this.createUserUseCase.execute(createUserRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDTO
                        .builder()
                        .message("Usuário cadastrado com sucesso!")
                        .data(result)
                        .build()
        );
    }

}
