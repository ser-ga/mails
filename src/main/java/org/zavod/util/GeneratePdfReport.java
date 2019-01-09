package org.zavod.util;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import org.zavod.model.MailEntity;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GeneratePdfReport {

    private static final String FONT = "./src/main/resources/static/fonts/times.ttf";
    private static final String LOGO = "./src/main/resources/static/images/logo.png";

    public static ByteArrayInputStream mailsReport(MailEntity mail) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter pdfWriter = new PdfWriter(out);

        PdfDocument pdfDocument = new PdfDocument(pdfWriter);

        PdfFont pdfFont = PdfFontFactory.createFont(FONT, PdfEncodings.IDENTITY_H);
        Rectangle r0 = new Rectangle(130, 750, 75, 60);
        Rectangle r1 = new Rectangle(20, 650, 300, 100);
        Rectangle r2 = new Rectangle(20, 550, 300, 155);
        Rectangle r3 = new Rectangle(350, 650, 200, 150);
        Rectangle r4 = new Rectangle(50, 550, 500, 30);
        Rectangle r5 = new Rectangle(50, 50, 500, 500);

        pdfDocument.addNewPage();

        PdfCanvas c1 = new PdfCanvas(pdfDocument.getFirstPage()).rectangle(r1);
        PdfCanvas c2 = new PdfCanvas(pdfDocument.getFirstPage()).rectangle(r2);
        PdfCanvas c3 = new PdfCanvas(pdfDocument.getFirstPage()).rectangle(r3);
        PdfCanvas c4 = new PdfCanvas(pdfDocument.getFirstPage()).rectangle(r4);
        PdfCanvas c5 = new PdfCanvas(pdfDocument.getFirstPage()).rectangle(r5);

        ImageData imageData = ImageDataFactory.create(LOGO);
        c5.addImage(imageData, r0, false);

        final char dml = (char) 171;
        final char dmr = (char) 187;

        Text title1 = new Text("Открытое акционерное общество".toUpperCase()).setFontSize(10);
        Text title2 = new Text("\n" + dml + "Радужный механический завод".toUpperCase() + dmr).setBold().setFontSize(12);
        Text innkpp = new Text("\nИНН 3311001852, КПП 331101001").setFontSize(10);
        Text address = new Text("\n123456 Московская обл., Люберецкий  р-н,\nг. Радужный, ул. Заводская, д.1").setFontSize(9);
        Text tel = new Text("\nТел./ факс. (12345) 1-23-45").setFontSize(9);
        Text email = new Text("\nE-mail:  mail@zavod.ru\n").setFontSize(9);
        Text number = new Text("\nИсх.№ " + dml + mail.getMailNumber() + dmr + " от " + mail.getCreateDate()).setFontSize(11);
        Text recipient = new Text(mail.getMailRecipient()).setBold().setFontSize(12);
        Text hello = new Text(mail.getMailText().substring(0, mail.getMailText().indexOf("\n") - 1)).setBold().setFontSize(12);
        Text content = new Text(mail.getMailText().substring(mail.getMailText().indexOf("\n") + 1)).setFontSize(12);

        Paragraph p1 = new Paragraph().setFont(pdfFont).setTextAlignment(TextAlignment.CENTER);
        Paragraph p2 = new Paragraph().setFont(pdfFont).setTextAlignment(TextAlignment.CENTER);
        Paragraph p3 = new Paragraph().setFont(pdfFont).setTextAlignment(TextAlignment.RIGHT);
        Paragraph p4 = new Paragraph().setFont(pdfFont).setTextAlignment(TextAlignment.CENTER);
        Paragraph p5 = new Paragraph().setFont(pdfFont).setTextAlignment(TextAlignment.JUSTIFIED).setFirstLineIndent(15);

        p1.add(title1);
        p1.add(title2);
        p1.add(innkpp);
        p2.add(address);
        p2.add(tel);
        p2.add(email);
        p2.add(number);
        p3.add(recipient);
        p4.add(hello);
        p5.add(content);

        new Canvas(c1, pdfDocument, r1).add(p1);
        new Canvas(c2, pdfDocument, r2).add(p2);
        new Canvas(c3, pdfDocument, r3).add(p3);
        new Canvas(c4, pdfDocument, r4).add(p4);
        new Canvas(c5, pdfDocument, r5).add(p5);

        pdfDocument.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
