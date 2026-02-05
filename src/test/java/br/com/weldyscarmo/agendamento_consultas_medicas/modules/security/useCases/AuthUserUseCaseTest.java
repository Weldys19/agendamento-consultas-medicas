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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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

    PatientEntity patientEntity;
    DoctorEntity doctorEntity;
    AuthRequestDTO authRequestDTO;
    TokenResponseDTO tokenResponseDTO;

    @BeforeEach
    void setup(){
        patientEntity = builderPatientEntity();
        doctorEntity = builderDoctorEntity();
        authRequestDTO = builderAuthRequestDTO();
        tokenResponseDTO = builderTokenResponseDTO();
    }

    @Nested
    class WhenCredentialsAreValid {

        @BeforeEach
        void setup(){
            when(passwordEncoder.matches(authRequestDTO.getPassword(), patientEntity.getPassword()))
                    .thenReturn(true);

            when(jwtGenerate.generateToken(any(AuthUserDTO.class)))
                    .thenReturn(tokenResponseDTO);
        }

        @Test
        public void itShouldBePossibleToAuthenticateThePatient() {

            when(patientRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                    .thenReturn(Optional.of(patientEntity));

            when(doctorRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                    .thenReturn(Optional.empty());

            TokenResponseDTO result = authUserUseCase.execute(authRequestDTO);

            assertThat(result).isNotNull();
            assertThat(result.getToken()).isEqualTo(tokenResponseDTO.getToken());
            assertThat(result.getExpiresAt()).isEqualTo(tokenResponseDTO.getExpiresAt());
        }

        @Test
        public void itShouldBePossibleToAuthenticateTheDoctor() {

            when(doctorRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                    .thenReturn(Optional.of(doctorEntity));

            when(patientRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                    .thenReturn(Optional.empty());

            TokenResponseDTO result = authUserUseCase.execute(authRequestDTO);

            assertThat(result).isNotNull();
            assertThat(result.getToken()).isEqualTo(tokenResponseDTO.getToken());
            assertThat(result.getExpiresAt()).isEqualTo(tokenResponseDTO.getExpiresAt());
        }
    }

    @Nested
    class WhenCredentialsAreInvalid {

        @Test
        public void shouldThrowExceptionWhenEmailDoesNotExist() {

            when(doctorRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                    .thenReturn(Optional.empty());

            when(patientRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> {
                authUserUseCase.execute(authRequestDTO);
            }).isInstanceOf(InvalidCredentialsException.class);
        }

        @Test
        public void shouldThrowExceptionWhenPasswordIsInvalid() {

            when(doctorRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                    .thenReturn(Optional.of(doctorEntity));

            when(patientRepository.findByEmailIgnoreCase(authRequestDTO.getEmail()))
                    .thenReturn(Optional.empty());

            when(passwordEncoder.matches(authRequestDTO.getPassword(), "hashPassword"))
                    .thenReturn(false);

            assertThatThrownBy(() -> {
                authUserUseCase.execute(authRequestDTO);
            }).isInstanceOf(InvalidCredentialsException.class);
        }
    }

    private PatientEntity builderPatientEntity(){
        return PatientEntity.builder()
                .id(UUID.randomUUID())
                .email("weldys@gmail.com")
                .password("hashPassword")
                .build();
    }

    private DoctorEntity builderDoctorEntity(){
        return DoctorEntity.builder()
                .id(UUID.randomUUID())
                .email("weldys@gmail.com")
                .password("hashPassword")
                .build();
    }

    private AuthRequestDTO builderAuthRequestDTO(){
        return AuthRequestDTO.builder()
                .email("weldys@gmail.com")
                .password("12345678910")
                .build();
    }

    private TokenResponseDTO builderTokenResponseDTO(){
        return TokenResponseDTO.builder()
                .token("token")
                .expiresAt(12345667L)
                .build();
    }
}
