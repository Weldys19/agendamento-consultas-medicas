package br.com.weldyscarmo.agendamento_consultas_medicas.modules.security.useCases;

import br.com.weldyscarmo.agendamento_consultas_medicas.exceptions.InvalidCredentialsException;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.DoctorEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.DoctorRepository;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.PatientEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.PatientRepository;
import br.com.weldyscarmo.agendamento_consultas_medicas.security.JWTGenerate;
import br.com.weldyscarmo.agendamento_consultas_medicas.security.dtos.AuthRequestDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.security.dtos.AuthUserDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.security.dtos.TokenResponseDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.security.useCases.AuthUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthUserUseCaseTest {

    @InjectMocks
    private AuthUserUseCase authUserUseCase;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JWTGenerate jwtGenerate;

    @Test
    public void itShouldBePossibleToAuthenticateThePatient(){

        PatientEntity patientEntity = PatientEntity.builder()
                .id(UUID.randomUUID())
                .email("weldys@gmail.com")
                .password("hashPassword")
                .build();

        AuthRequestDTO authRequestDTO = AuthRequestDTO.builder()
                .email("weldys@gmail.com")
                .password("12345678910")
                .build();

        TokenResponseDTO token = TokenResponseDTO.builder()
                .token("token")
                .expiresAt(12345667L)
                .build();

        when(this.patientRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                .thenReturn(Optional.of(patientEntity));

        when(this.doctorRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                .thenReturn(Optional.empty());

        when(this.passwordEncoder.matches(authRequestDTO.getPassword(), patientEntity.getPassword()))
                .thenReturn(true);

        when(this.jwtGenerate.generateToken(any(AuthUserDTO.class)))
                .thenReturn(token);

        TokenResponseDTO result = this.authUserUseCase.execute(authRequestDTO);

        assertNotNull(result);
        assertThat(result.getToken()).isEqualTo(token.getToken());
        assertThat(result.getExpiresAt()).isEqualTo(token.getExpiresAt());
    }
    @Test
    public void itShouldBePossibleToAuthenticateTheDoctor(){

        DoctorEntity doctorEntity = DoctorEntity.builder()
                .id(UUID.randomUUID())
                .email("weldys@gmail.com")
                .password("hashPassword")
                .build();

        AuthRequestDTO authRequestDTO = AuthRequestDTO.builder()
                .email("weldys@gmail.com")
                .password("12345678910")
                .build();

        TokenResponseDTO token = TokenResponseDTO.builder()
                .token("token")
                .expiresAt(1L)
                .build();

        when(this.doctorRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                .thenReturn(Optional.of(doctorEntity));

        when(this.patientRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                .thenReturn(Optional.empty());

        when(this.passwordEncoder.matches(authRequestDTO.getPassword(), doctorEntity.getPassword()))
                .thenReturn(true);

        when(this.jwtGenerate.generateToken(any(AuthUserDTO.class)))
                .thenReturn(token);

        TokenResponseDTO result = this.authUserUseCase.execute(authRequestDTO);

        assertNotNull(result);
        assertThat(result.getToken()).isEqualTo(token.getToken());
        assertThat(result.getExpiresAt()).isEqualTo(token.getExpiresAt());
    }

    @Test
    public void shouldThrowExceptionWhenEmailDoesNotExist(){

        AuthRequestDTO authRequestDTO = AuthRequestDTO.builder()
                .email("weldys@gmail.com")
                .password("12345678910")
                .build();

        when(this.doctorRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                .thenReturn(Optional.empty());

        when(this.patientRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                .thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () ->
                this.authUserUseCase.execute(authRequestDTO));
    }

    @Test
    public void shouldThrowExceptionWhenPasswordIsInvalid(){

        DoctorEntity doctorEntity = DoctorEntity.builder()
                .id(UUID.randomUUID())
                .email("weldys@gmail.com")
                .password("hashPassword")
                .build();

        AuthRequestDTO authRequestDTO = AuthRequestDTO.builder()
                .email("weldys@gmail.com")
                .password("12345678910")
                .build();

        when(this.doctorRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                .thenReturn(Optional.of(doctorEntity));

        when(this.patientRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                .thenReturn(Optional.empty());

        when(this.passwordEncoder.matches(authRequestDTO.getPassword(), "hashPassword"))
                .thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () ->
                this.authUserUseCase.execute(authRequestDTO));
    }
}
