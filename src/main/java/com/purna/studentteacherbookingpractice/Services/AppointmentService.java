package com.purna.studentteacherbookingpractice.Services;

import com.purna.studentteacherbookingpractice.Models.Appointment;
import com.purna.studentteacherbookingpractice.Models.Mail;
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
    @Autowired
    private MailService mailService;

    public boolean saveAppointment(Appointment appointment) throws Exception {
        if(appointment.getDate().isBefore(LocalDate.now())){
            return false;
        }

        appointment.setStatus("Pending");
        appointment.setStudentId(userService.getAuthenticatedUser().getId());
        appointment.setStudentName(userService.getAuthenticatedUser().getFullname());
        Users student=userService.findById(appointment.getStudentId());
        Users teacher=userService.findById(appointment.getTeacherId());
        String msgBody = "<div style='font-family: Arial, sans-serif; color: #333; max-width: 600px; margin: auto;'>"
                + "<div style='background-color: #f7f7f7; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);'>"
                + "<h2 style='color: #12A0DCFF; text-align: center;'>Appointment Request</h2>"
                + "<hr style='border: 0; border-top: 3px solid #12A0DCFF; margin: 20px 0;'>"
                + "<p style='font-size: 16px; color: #333; line-height: 1.5;'>Dear " + teacher.getFullname() + ",</p>"
                + "<p style='font-size: 16px; color: #333; line-height: 1.5;'>You have received an appointment request from " + student.getFullname() + ".</p>"
                + "<p style='font-size: 16px; color: #333; line-height: 1.5;'>Here are the details of the appointment:</p>"
                + "<ul style='font-size: 16px; color: #333; line-height: 1.5;'>"
                + "<li><strong>Date:</strong> " + appointment.getDate() + "</li>"
                + "<li><strong>Time:</strong> " + appointment.getTime() + "</li>"
                + "<li><strong>Purpose:</strong> " + appointment.getPurpose() + "</li>"
                + "</ul>"
                + "<p style='font-size: 16px; color: #333; line-height: 1.5;'>Please review and respond to this request at your earliest convenience.</p>"
                + "<div style='text-align: center; margin-top: 20px;'>"
                + "<a href='http://localhost:8080/approve/" + appointment.getAppointmentId() + "' style='display: inline-block; padding: 10px 20px; margin-right: 10px; background-color: #4CAF50; color: #fff; text-decoration: none; border-radius: 5px;'>Approve</a>"
                + "<a href='http://localhost:8080/reject/" + appointment.getAppointmentId() + "' style='display: inline-block; padding: 10px 20px; background-color: #f44336; color: #fff; text-decoration: none; border-radius: 5px;'>Reject</a>"
                + "</div>"
                + "<p style='font-size: 16px; color: #333; line-height: 1.5;'>Sincerely,<br>API Pvt. Ltd.</p>"
                + "<hr style='border: 0; border-top: 1px solid #ccc; margin: 20px 0;'>"
                + "<p style='font-size: 12px; color: #333; text-align: center;'>If you did not request this appointment, please disregard this email.</p>"
                + "</div>"
                + "</div>";
        Mail email=new Mail(teacher.getEmail(),
                "New Appointment",
                msgBody);
        mailService.sendRegistrationMail(email);

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
