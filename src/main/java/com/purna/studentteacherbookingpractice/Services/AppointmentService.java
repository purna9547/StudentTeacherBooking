package com.purna.studentteacherbookingpractice.Services;

import com.purna.studentteacherbookingpractice.Models.Appointment;
import com.purna.studentteacherbookingpractice.Repositries.AppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AppointmentService {
    @Autowired
    private UserService userService;
    @Autowired
    private AppointmentRepo appointmentRepo;

    public boolean saveAppointment(Appointment appointment) {
        if(appointment.getDate().isBefore(LocalDate.now())){
            return false;
        }

        appointment.setStatus("Pending");
        appointment.setStudentId(userService.getAuthenticatedUser().getId());
        appointment.setStudentName(userService.getAuthenticatedUser().getFullname());

        appointmentRepo.save(appointment);
        return true;

    }
}
