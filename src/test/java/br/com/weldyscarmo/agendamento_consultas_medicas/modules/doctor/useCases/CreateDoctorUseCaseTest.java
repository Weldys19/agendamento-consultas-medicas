package br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.useCases;

import br.com.weldyscarmo.agendamento_consultas_medicas.exceptions.UserFoundException;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.DoctorEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.DoctorRepository;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.dtos.CreateDoctorRequestDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor.dtos.DoctorResponseDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.PatientRepository;
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
public class CreateDoctorUseCaseTest {

    @InjectMocks
    private CreateDoctorUseCase createDoctorUseCase;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PatientRepository patientRepository;

    CreateDoctorRequestDTO createDoctorRequestDTO;
    DoctorEntity doctorEntity;

    @BeforeEach
    void setup(){
        createDoctorRequestDTO = builderCreateDoctorRequest();
        doctorEntity = DoctorEntity.builder()
                .id(UUID.randomUUID())
                .build();
    }

    @Test
    public void itShouldBePossibleToCreateADoctor(){

        when(this.doctorRepository.findByEmailIgnoreCase(createDoctorRequestDTO.getEmail()))
                .thenReturn(Optional.empty());

        when(this.patientRepository.findByEmailIgnoreCase(createDoctorRequestDTO.getEmail()))
                .thenReturn(Optional.empty());

        when(this.passwordEncoder.encode(createDoctorRequestDTO.getPassword()))
                .thenReturn("hashPassword");

        when(this.doctorRepository.save(any(DoctorEntity.class))).thenReturn(doctorEntity);

        DoctorResponseDTO result = this.createDoctorUseCase.execute(createDoctorRequestDTO);

        assertThat(result.getId()).isEqualTo(doctorEntity.getId());
        verify(passwordEncoder).encode(any(String.class));
        verify(doctorRepository).save(any(DoctorEntity.class));
    }

    @Test
    public void itShouldNotBePossibleToCreateADoctor(){

        when(this.doctorRepository.findByEmailIgnoreCase(createDoctorRequestDTO.getEmail()))
                .thenReturn(Optional.of(doctorEntity));

        assertThatThrownBy(() -> {
            this.createDoctorUseCase.execute(createDoctorRequestDTO);
        }).isInstanceOf(UserFoundException.class);
    }

    private CreateDoctorRequestDTO builderCreateDoctorRequest(){
        return CreateDoctorRequestDTO.builder()
                .email("weldys@gmail.com")
                .password("1234567890")
                .build();
    }
}
