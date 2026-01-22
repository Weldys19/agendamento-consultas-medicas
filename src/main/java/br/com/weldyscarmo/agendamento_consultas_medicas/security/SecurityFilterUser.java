package br.com.weldyscarmo.agendamento_consultas_medicas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SecurityFilterUser extends OncePerRequestFilter {

    @Autowired
    private JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var header = request.getHeader("Authorization");

        if (header != null){
            var tokenDecoded = this.jwtProvider.validateToken(header.substring(7));

            request.setAttribute("user_id", tokenDecoded.getSubject());
            var roles = tokenDecoded.getClaim("roles").asString();

            var permissions = new SimpleGrantedAuthority("ROLE_" + roles);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    tokenDecoded.getSubject(), null, List.of(permissions)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
