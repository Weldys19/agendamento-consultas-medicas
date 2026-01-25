package br.com.weldyscarmo.agendamento_consultas_medicas.modules.appointments;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.UUID;

public interface AppointmentsRepository extends JpaRepository<AppointmentsEntity, UUID> {

    List<AppointmentsEntity> findAllByDoctorIdAndDay(UUID doctorId, DayOfWeek day);
}
