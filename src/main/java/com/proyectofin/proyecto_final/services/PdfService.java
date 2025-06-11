package com.proyectofin.proyecto_final.services;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.proyectofin.proyecto_final.entities.Terreno;

@Service
public class PdfService {

    public byte[] generateTerrenoPdf(Terreno terreno) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Configuración de fuentes
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
            
            // Título del documento
            Paragraph title = new Paragraph("Detalles del Terreno", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Información del terreno
            document.add(new Paragraph("ID del Terreno: " + terreno.getId(), subtitleFont));
            document.add(new Paragraph("Superficie: " + terreno.getSuperficie() + " hectáreas", normalFont));
            document.add(new Paragraph("Cultivo: " + terreno.getCultivo(), normalFont));
            
            // Información del propietario
            document.add(new Paragraph("\nInformación del Propietario:", subtitleFont));
            document.add(new Paragraph("ID del Usuario: " + terreno.getUser().getId(), normalFont));
            document.add(new Paragraph("Nombre: " + terreno.getUser().getName(), normalFont));
            document.add(new Paragraph("Email: " + terreno.getUser().getEmail(), normalFont));
            
            // Tabla con detalles adicionales
            document.add(new Paragraph("\nDetalles Adicionales:", subtitleFont));
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            // Encabezados de la tabla
            PdfPCell cell = new PdfPCell(new Phrase("Característica"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
            
            cell = new PdfPCell(new Phrase("Valor"));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
            
            // Datos de la tabla
            table.addCell("Fecha de Registro");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            table.addCell(sdf.format(new Date()));
            
            table.addCell("Tipo de Cultivo");
            table.addCell(terreno.getCultivo());
            
            table.addCell("Superficie");
            table.addCell(terreno.getSuperficie() + " hectáreas");
            
            document.add(table);
            
            // Pie de página
            Paragraph footer = new Paragraph("Documento generado el " + sdf.format(new Date()), normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);
            
            document.close();
            
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF: " + e.getMessage(), e);
        }
    }
}