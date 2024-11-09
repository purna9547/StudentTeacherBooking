package com.purna.studentteacherbookingpractice.Repositries;

import com.purna.studentteacherbookingpractice.Models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, UUID> {
}
