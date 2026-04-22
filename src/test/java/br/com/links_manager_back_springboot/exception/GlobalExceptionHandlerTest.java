package br.com.links_manager_back_springboot.exception;

import br.com.links_manager_back_springboot.dto.ApiResponseDTO;
import br.com.links_manager_back_springboot.dto.FieldErrorDTO;
import br.com.links_manager_back_springboot.module.user.dto.CreateUserRequestDTO;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Deve retornar status 500 com a mensagem da RuntimeException")
    void shouldReturnInternalServerErrorWithExceptionMessage() {
        RuntimeException runtimeException = new RuntimeException("Falha inesperada");

        ResponseEntity<ApiResponseDTO> response = globalExceptionHandler.handleRuntimeException(runtimeException);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Falha inesperada", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    @DisplayName("Deve retornar status 400 com lista de campos inválidos")
    void shouldReturnBadRequestWhenMethodArgumentNotValidExceptionOccurs() {
        CreateUserRequestDTO target = new CreateUserRequestDTO();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "createUserRequestDTO");
        bindingResult.rejectValue("email", "invalid", "E-mail inválido");

        MethodArgumentNotValidException methodArgumentNotValidException =
                new MethodArgumentNotValidException(mock(MethodParameter.class), bindingResult);

        ResponseEntity<ApiResponseDTO> response =
                globalExceptionHandler.handleMethodArgumentNotValidException(methodArgumentNotValidException);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Campos inválidos.", response.getBody().getMessage());
        assertInstanceOf(List.class, response.getBody().getData());

        @SuppressWarnings("unchecked")
        List<FieldErrorDTO> fieldErrors = (List<FieldErrorDTO>) response.getBody().getData();
        assertEquals(1, fieldErrors.size());
        assertEquals("email", fieldErrors.get(0).getField());
        assertEquals("E-mail inválido", fieldErrors.get(0).getMessage());
    }

    @Test
    @DisplayName("Deve retornar status 422 quando a entidade já existir")
    void shouldReturnUnprocessableEntityWhenEntityAlreadyExists() {
        EntityExistsException entityExistsException = new EntityExistsException("Este e-mail e/ou usuário já existe.");

        ResponseEntity<ApiResponseDTO> response = globalExceptionHandler.handleEntityExistsException(entityExistsException);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals("Este e-mail e/ou usuário já existe.", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
