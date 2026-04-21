package br.com.links_manager_back_springboot.module.user.dto;

import br.com.links_manager_back_springboot.annotation.ValidEmail;
import br.com.links_manager_back_springboot.annotation.ValidPassword;
import br.com.links_manager_back_springboot.annotation.ValidUsername;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequestDTO {

    @ValidUsername
    private String username;

    @ValidEmail
    private String email;

    @ValidPassword
    private String password;

}
