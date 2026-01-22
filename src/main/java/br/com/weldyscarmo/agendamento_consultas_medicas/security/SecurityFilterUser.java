package br.com.weldyscarmo.agendamento_consultas_medicas.security;

import br.com.weldyscarmo.agendamento_consultas_medicas.exceptions.InvalidTokenException;
import com.auth0.jwt.interfaces.DecodedJWT;
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

        String header = request.getHeader("Authorization");

        if (header != null){

            try {
                DecodedJWT tokenDecoded = this.jwtProvider.validateToken(header.substring(7));

                request.setAttribute("user_id", tokenDecoded.getSubject());
                String roles = tokenDecoded.getClaim("roles").asString();

                SimpleGrantedAuthority permissions = new SimpleGrantedAuthority("ROLE_" + roles);

                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        tokenDecoded.getSubject(), null, List.of(permissions)
                );
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (InvalidTokenException e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            }
        }
        filterChain.doFilter(request, response);
    }
}
