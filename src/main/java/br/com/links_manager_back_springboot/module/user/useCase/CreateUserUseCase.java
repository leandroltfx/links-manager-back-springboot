package br.com.links_manager_back_springboot.module.user.useCase;

import br.com.links_manager_back_springboot.module.user.dto.CreateUserRequestDTO;
import br.com.links_manager_back_springboot.module.user.dto.CreateUserResponseDTO;
import br.com.links_manager_back_springboot.module.user.entity.UserEntity;
import br.com.links_manager_back_springboot.module.user.repository.UserRepository;
import br.com.links_manager_back_springboot.service.JWTService;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    public CreateUserResponseDTO execute(
            CreateUserRequestDTO createUserRequestDTO
    ) {
        try {
            UserEntity userEntity = UserEntity
                    .builder()
                    .username(createUserRequestDTO.getUsername())
                    .email(createUserRequestDTO.getEmail())
                    .password(this.passwordEncoder.encode(createUserRequestDTO.getPassword()))
                    .build();
            var createdUser = this.userRepository.save(userEntity);
            return CreateUserResponseDTO
                    .builder()
                    .accessToken(this.jwtService.generateToken(createdUser.getId()))
                    .build();
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new EntityExistsException("Este nome de usuário ou e-mail já está em uso.");
        } catch (Exception exception) {
            throw new RuntimeException("Ocorreu um erro durante o cadastro, tente novamente.");
        }
    }
}

