package br.com.links_manager_back_springboot.filter;

import br.com.links_manager_back_springboot.dto.ApiResponseDTO;
import br.com.links_manager_back_springboot.service.JWTService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null) {
            var subjectToken = this.jwtService.validateToken(header);
            if (subjectToken.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                ApiResponseDTO apiResponseDTO = ApiResponseDTO
                        .builder()
                        .message("Token inválido ou expirado.")
                        .data(null)
                        .build();
                ObjectMapper mapper = new ObjectMapper();
                response.getWriter().write(mapper.writeValueAsString(apiResponseDTO));
                return;
            }

            request.setAttribute("user_id", subjectToken);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    subjectToken,
                    null,
                    Collections.emptyList()
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            if (!request.getRequestURI().contains("login") && (!request.getMethod().equals("POST") || !request.getRequestURI().contains("users"))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                ApiResponseDTO apiResponseDTO = ApiResponseDTO
                        .builder()
                        .message("Informe o token.")
                        .data(null)
                        .build();
                ObjectMapper mapper = new ObjectMapper();
                response.getWriter().write(mapper.writeValueAsString(apiResponseDTO));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
