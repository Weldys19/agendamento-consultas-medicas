package br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.useCases;

import br.com.weldyscarmo.agendamento_consultas_medicas.exceptions.UserFoundException;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.DoctorRepository;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.PatientEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.PatientRepository;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.dtos.CreatePatientRequestDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.dtos.PatientResponseDTO;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreatePatientUseCaseTest {

    @InjectMocks
    private CreatePatientUseCase createPatientUseCase;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DoctorRepository doctorRepository;

    CreatePatientRequestDTO createPatientRequestDTO;
    PatientEntity patientEntity;

    @BeforeEach
    void setup(){
        createPatientRequestDTO = builderCreatePatientRequest();
        patientEntity = PatientEntity.builder()
                .id(UUID.randomUUID())
                .build();
    }

    @Test
    public void itShouldBePossibleToCreateAPatient(){

        when(this.patientRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(createPatientRequestDTO.getUsername(),
                createPatientRequestDTO.getEmail())).thenReturn(Optional.empty());

        when(this.doctorRepository.findByEmailIgnoreCase(createPatientRequestDTO.getEmail()))
                .thenReturn(Optional.empty());

        when(this.passwordEncoder.encode(createPatientRequestDTO.getPassword())).thenReturn("hashPassword");

        when(this.patientRepository.save(any(PatientEntity.class))).thenReturn(patientEntity);

        PatientResponseDTO result = this.createPatientUseCase.execute(createPatientRequestDTO);

        assertThat(result.getId()).isEqualTo(patientEntity.getId());
        verify(patientRepository).save(any(PatientEntity.class));
        verify(passwordEncoder).encode(any(String.class));
    }

    @Test
    public void itShouldNotBePossibleToCreateAPatient(){

        when(this.patientRepository.findByUsernameIgnoreCaseOrEmailIgnoreCase(createPatientRequestDTO.getUsername(),
                createPatientRequestDTO.getEmail())).thenReturn(Optional.of(patientEntity));

        assertThatThrownBy(() -> {
           createPatientUseCase.execute(createPatientRequestDTO);
        }).isInstanceOf(UserFoundException.class);
    }

    private CreatePatientRequestDTO builderCreatePatientRequest() {
        return CreatePatientRequestDTO.builder()
                .name("Weldys")
                .email("weldyscarmo@gmail.com")
                .username("WeldysdoCarmo")
                .password("12345678910")
                .build();
    }
}
