package com.visionflow.core.appointment.repo;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */

import com.visionflow.core.appointment.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.id = :id AND a.deleted = false")
    Optional<Appointment> findByIdAndDeletedFalse(@Param("id") Long id);

    @Query("""
            SELECT COUNT(a) > 0 FROM Appointment a
            WHERE a.doctor.id = :doctorId
            AND a.deleted = false
            AND a.status NOT IN (com.visionflow.core.appointment.enums.AppointmentStatus.CANCELLED,
                                 com.visionflow.core.appointment.enums.AppointmentStatus.NO_SHOW)
            AND a.appointmentDate < :end
            AND a.appointmentDate > :start
            """)
    boolean existsDoctorConflict(@Param("doctorId") Long doctorId,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);
}
