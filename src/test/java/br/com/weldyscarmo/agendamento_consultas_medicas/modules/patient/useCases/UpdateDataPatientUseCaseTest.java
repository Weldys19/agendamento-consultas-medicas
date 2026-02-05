package br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.useCases;

import br.com.weldyscarmo.agendamento_consultas_medicas.exceptions.UserNotFoundException;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.PatientEntity;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.PatientRepository;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.dtos.PatientResponseDTO;
import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.dtos.UpdateDataPatientRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateDataPatientUseCaseTest {

    @InjectMocks
    private UpdateDataPatientUseCase updateDataPatientUseCase;

    @Mock
    private PatientRepository patientRepository;


    @Nested
    class WhenPatientExists {

        PatientEntity patientEntity;
        UpdateDataPatientRequestDTO updateDataPatientRequestDTO;

        @BeforeEach
        void setup(){
            patientEntity = builderPatientEntity();
            updateDataPatientRequestDTO = builderUpdateDataPatientRequest();

            when(patientRepository.findById(patientEntity.getId()))
                    .thenReturn(Optional.of(patientEntity));
        }

        @Test
        public void shouldUpdatePatientNameAndUsername() {
            PatientResponseDTO result = updateDataPatientUseCase.execute(updateDataPatientRequestDTO,
                    patientEntity.getId());

            assertThat(result.getName()).isEqualTo(updateDataPatientRequestDTO.getName());
            assertThat(result.getUsername()).isEqualTo(updateDataPatientRequestDTO.getUsername());
        }

        @Test
        public void shouldUpdatePatientName() {
            PatientResponseDTO result = updateDataPatientUseCase.execute(updateDataPatientRequestDTO,
                    patientEntity.getId());

            assertThat(result.getName()).isEqualTo(updateDataPatientRequestDTO.getName());
            assertThat(result.getUsername()).isEqualTo(patientEntity.getUsername());
        }

        @Test
        public void shouldUpdatePatientUsername() {
            PatientResponseDTO result = updateDataPatientUseCase.execute(updateDataPatientRequestDTO,
                    patientEntity.getId());

            assertThat(result.getName()).isEqualTo(patientEntity.getName());
            assertThat(result.getUsername()).isEqualTo(updateDataPatientRequestDTO.getUsername());
        }
    }

    @Nested
    class WhenPatientNotExists {

        @Test
        public void shouldNotUpdatePatient() {
            when(patientRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> {
                updateDataPatientUseCase.execute(new UpdateDataPatientRequestDTO(), UUID.randomUUID());
            }).isInstanceOf(UserNotFoundException.class);
        }
    }

    private PatientEntity builderPatientEntity(){
        return PatientEntity.builder()
                .id(UUID.randomUUID())
                .name("weldys")
                .username("weldyscarmo")
                .build();
    }

    private UpdateDataPatientRequestDTO builderUpdateDataPatientRequest(){
        return UpdateDataPatientRequestDTO.builder()
                .name("weldys do carmo")
                .username("weldys002")
                .build();
    }
}
