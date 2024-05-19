package com.asm3.pdf;

import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.TemplateEngine;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.asm3.dto.DoctorDTO;
import com.asm3.dto.UserDTO;
import com.asm3.entity.Clinic;
import com.asm3.entity.Doctor;
import com.asm3.entity.Patient;
import com.asm3.entity.Schedule;
import com.asm3.entity.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class PdfGenerator {

    public static File generatePdfFromHtml(String htmlContent, String pdfPath) throws Exception {
        Context context = new Context();
        
        TemplateEngine templateEngine = new TemplateEngine();
        String processedHtml = templateEngine.process(htmlContent, context);

        ITextRenderer renderer = new ITextRenderer();
        
        renderer.setDocumentFromString(processedHtml);
        renderer.layout();

        try (OutputStream outputStream = new FileOutputStream(pdfPath)) {
            renderer.createPDF(outputStream);
        }
        
        return new File(pdfPath);
    }

    public static File createPDF(Schedule schedule) {
        try {
        	User user = schedule.getUser();
        	UserDTO userDTO = UserDTO.convertToDTO(user);
        	Doctor doctor = schedule.getDoctor();
        	DoctorDTO doctorDTO = DoctorDTO.convertToDTO(doctor);
        	Clinic clinic = doctor.getClinic();
        	Patient patient = schedule.getPatient();
        	patient.setSchedule(null);
        	
            ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
            templateResolver.setPrefix("templates/pdf/");
            templateResolver.setSuffix(".html");
            templateResolver.setTemplateMode(TemplateMode.HTML.toString());
             
            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(templateResolver);
             
            Context context = new Context();
            context.setVariable("clinicName", clinic.getName());
            context.setVariable("uesrName", user.getFullName());
            context.setVariable("scheduleDate", schedule.getDate());
            context.setVariable("patientDes", patient.getDecExam());
            
            
            String html = templateEngine.process("genPDF", context);
            
            File pdfFile = generatePdfFromHtml(html, "examatied.pdf");
            System.out.println("PDF created successfully.");
            return pdfFile;
        } catch (Exception e) {
            return null;
        }
    }
    
}
