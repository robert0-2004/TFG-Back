package com.proyectofin.proyecto_final.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.proyectofin.proyecto_final.entities.User;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Bienvenido a nuestra plataforma");
        message.setText("Hola " + user.getName() + ",\n\n" +
                "Gracias por registrarte en nuestra plataforma. Tu cuenta ha sido creada exitosamente.\n\n" +
                "Saludos,\nEl equipo de soporte");
                    
        mailSender.send(message);
    }
}