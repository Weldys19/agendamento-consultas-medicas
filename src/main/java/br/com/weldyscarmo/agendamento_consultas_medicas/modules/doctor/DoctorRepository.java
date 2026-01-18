package br.com.weldyscarmo.agendamento_consultas_medicas.modules.doctor;

import br.com.weldyscarmo.agendamento_consultas_medicas.modules.patient.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<DoctorEntity, UUID> {
    Optional<DoctorEntity> findByEmailIgnoreCase(String email);
}
