package com.purna.studentteacherbookingpractice.Services;

import com.purna.studentteacherbookingpractice.Models.Appointment;
import com.purna.studentteacherbookingpractice.Models.Users;
import com.purna.studentteacherbookingpractice.Repositries.AppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<Appointment> getAllAppointment(UUID id) {
        List<Appointment>allAppointment=appointmentRepo.findAll();
        List<Appointment>teacherAppointment= new ArrayList<>();

        for (Appointment appointment:allAppointment){
            if (appointment.getTeacherId().equals(id) && appointment.getStatus().equals("Pending")&& appointment.getDate().isAfter(LocalDate.now())){
                appointment.setStudentName(userService.findById(appointment.getStudentId()).getFullname());
                teacherAppointment.add(appointment);

            }
        }
        return teacherAppointment;
    }

    public void approve(UUID id) {
        Appointment appointment=appointmentRepo.findById(id).get();
        Users student= userService.findById(appointment.getStudentId());
        Users teacher= userService.findById(appointment.getTeacherId());
        appointment.setStatus("Approved");
        appointmentRepo.save(appointment);
    }

    public void reject(UUID id) {
        Appointment appointment=appointmentRepo.findById(id).get();
        Users student =userService.findById(appointment.getStudentId());
        Users teacher= userService.findById(appointment.getTeacherId());
        appointmentRepo.deleteById(id);
    }

    public List<Appointment> getPastUser(UUID id) {
        List<Appointment> allAppointment = appointmentRepo.findAll();
        return allAppointment.stream()
                .filter(appointment ->
                        appointment.getTeacherId().equals(id)
                                && appointment.getStatus().equals("Approved")
                                && appointment.getDate().isBefore(LocalDate.now())
                )
                .sorted((appointment1, appointment2) -> appointment2.getDate().compareTo(appointment1.getDate()))
                .collect(Collectors.toList());
    }

    public List<Appointment> getUpcomingUser(UUID id) {
        List<Appointment> allAppointment = appointmentRepo.findAll();
        return allAppointment.stream()
                .filter(appointment ->
                        appointment.getTeacherId().equals(id)
                                && appointment.getStatus().equals("Approved")
                                && (appointment.getDate().isAfter(LocalDate.now()) || appointment.getDate().isEqual(LocalDate.now()))
                )
                .sorted(Comparator.comparing(Appointment::getDate).thenComparing(Appointment::getTime))
                .collect(Collectors.toList());

    }
}
