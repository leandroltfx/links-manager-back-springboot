package br.com.links_manager_back_springboot.module.user.useCase;

import br.com.links_manager_back_springboot.module.user.dto.CreateUserRequestDTO;
import br.com.links_manager_back_springboot.module.user.dto.CreateUserResponseDTO;
import br.com.links_manager_back_springboot.module.user.entity.UserEntity;
import br.com.links_manager_back_springboot.module.user.repository.UserRepository;
import br.com.links_manager_back_springboot.service.JWTService;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    @DisplayName("Deve criar um usuário e retornar o token de acesso")
    void shouldCreateUserAndReturnAccessToken() {
        CreateUserRequestDTO requestDTO = CreateUserRequestDTO.builder()
                .username("usuario_teste")
                .email("usuario@teste.com")
                .password("senha123")
                .build();

        UUID createdUserId = UUID.randomUUID();
        UserEntity createdUser = UserEntity.builder()
                .id(createdUserId)
                .username(requestDTO.getUsername())
                .email(requestDTO.getEmail())
                .password("senha-criptografada")
                .build();

        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("senha-criptografada");
        when(userRepository.save(org.mockito.ArgumentMatchers.any(UserEntity.class))).thenReturn(createdUser);
        when(jwtService.generateToken(createdUserId)).thenReturn("jwt-token");

        CreateUserResponseDTO responseDTO = createUserUseCase.execute(requestDTO);

        assertEquals("jwt-token", responseDTO.getAccessToken());

        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());

        UserEntity userToSave = userCaptor.getValue();
        assertEquals(requestDTO.getUsername(), userToSave.getUsername());
        assertEquals(requestDTO.getEmail(), userToSave.getEmail());
        assertEquals("senha-criptografada", userToSave.getPassword());
    }

    @Test
    @DisplayName("Deve lançar EntityExistsException quando houver violação de integridade")
    void shouldThrowEntityExistsExceptionWhenDataIntegrityViolationOccurs() {
        CreateUserRequestDTO requestDTO = CreateUserRequestDTO.builder()
                .username("duplicado")
                .email("duplicado@teste.com")
                .password("senha123")
                .build();

        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("senha-criptografada");
        when(userRepository.save(org.mockito.ArgumentMatchers.any(UserEntity.class)))
                .thenThrow(new DataIntegrityViolationException("violacao de integridade"));

        EntityExistsException exception = assertThrows(EntityExistsException.class,
                () -> createUserUseCase.execute(requestDTO));

        assertEquals("Este nome de usuário ou e-mail já está em uso.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar RuntimeException com mensagem amigável em erro inesperado")
    void shouldThrowRuntimeExceptionWithFriendlyMessageWhenUnexpectedErrorOccurs() {
        CreateUserRequestDTO requestDTO = CreateUserRequestDTO.builder()
                .username("usuario")
                .email("usuario@teste.com")
                .password("senha123")
                .build();

        when(passwordEncoder.encode(requestDTO.getPassword())).thenReturn("senha-criptografada");
        when(userRepository.save(org.mockito.ArgumentMatchers.any(UserEntity.class)))
                .thenThrow(new IllegalStateException("erro inesperado"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> createUserUseCase.execute(requestDTO));

        assertEquals("Ocorreu um erro durante o cadastro, tente novamente.", exception.getMessage());
        assertTrue(exception.getCause() == null);
    }
}