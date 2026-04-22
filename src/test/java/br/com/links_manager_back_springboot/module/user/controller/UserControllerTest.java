package br.com.links_manager_back_springboot.module.user.controller;

import br.com.links_manager_back_springboot.dto.ApiResponseDTO;
import br.com.links_manager_back_springboot.module.user.dto.CreateUserRequestDTO;
import br.com.links_manager_back_springboot.module.user.dto.CreateUserResponseDTO;
import br.com.links_manager_back_springboot.module.user.useCase.CreateUserUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private CreateUserUseCase createUserUseCase;

    @InjectMocks
    private UserController userController;

    @Test
    @DisplayName("Deve retornar status 201 com mensagem de sucesso ao cadastrar usuário")
    void shouldReturnCreatedStatusAndSuccessMessageWhenCreatingUser() {
        CreateUserRequestDTO requestDTO = CreateUserRequestDTO.builder()
                .username("usuario_teste")
                .email("usuario@teste.com")
                .password("Senha@123")
                .build();

        CreateUserResponseDTO responseDTO = CreateUserResponseDTO.builder()
                .accessToken("jwt-token")
                .build();

        when(createUserUseCase.execute(requestDTO)).thenReturn(responseDTO);

        ResponseEntity<ApiResponseDTO> response = userController.createUser(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Usuário cadastrado com sucesso!", response.getBody().getMessage());
        assertEquals(responseDTO, response.getBody().getData());
        verify(createUserUseCase).execute(requestDTO);
    }

    @Test
    @DisplayName("Deve encaminhar os dados recebidos para o caso de uso de criação de usuário")
    void shouldForwardReceivedDataToCreateUserUseCase() {
        CreateUserRequestDTO requestDTO = CreateUserRequestDTO.builder()
                .username("outro_usuario")
                .email("outro@teste.com")
                .password("Senha@123")
                .build();

        CreateUserResponseDTO responseDTO = CreateUserResponseDTO.builder()
                .accessToken("outro-token")
                .build();

        when(createUserUseCase.execute(requestDTO)).thenReturn(responseDTO);

        userController.createUser(requestDTO);

        verify(createUserUseCase).execute(requestDTO);
    }
}
