package org.zavod.util.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.zavod.model.MailEntity;

import java.io.*;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

@Component
public class HtmlPdfReport implements IPdfReport<MailEntity> {

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public byte[] create(MailEntity mail) {

        final Resource font = resourceLoader.getResource(CLASSPATH_URL_PREFIX + "static/fonts/times.ttf");
        final Resource imagesPath = resourceLoader.getResource(CLASSPATH_URL_PREFIX + "static/images");
        final Resource templateFolder = resourceLoader.getResource(CLASSPATH_URL_PREFIX + "templates/freemarker");

        try (StringWriter htmlWriter = new StringWriter(); ByteArrayOutputStream outPdf = new ByteArrayOutputStream()) {

            /* FreeMarker */

            // Config
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
            TemplateLoader templateLoader = new FileTemplateLoader(templateFolder.getFile());
            cfg.setTemplateLoader(templateLoader);
            cfg.setDefaultEncoding("UTF-8");
            // Model
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("mail", mail);
            modelMap.put("mailDate", mail.getCreateDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            // Template
            Template temp = cfg.getTemplate("mail.ftl");
            // Process
            temp.process(modelMap, htmlWriter);

            /* iTextPdf */

            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outPdf);

            document.open();

            XMLWorkerFontProvider xmlWorkerFontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
            xmlWorkerFontProvider.register(font.getFile().getAbsolutePath());
            FontFactory.setFontImp(xmlWorkerFontProvider);

            // windows-1251 - times.ttf encoding for windows
            // UTF-8 - for linux
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(htmlWriter.toString().getBytes()),
                    null, Charset.forName("windows-1251"), xmlWorkerFontProvider, imagesPath.getFile().getAbsolutePath());

            document.close();

            PdfReader pdfReader = new PdfReader(outPdf.toByteArray());
            outPdf.reset();
            PdfStamper pdfStamper = new PdfStamper(pdfReader, outPdf);
            PdfContentByte canvas = pdfStamper.getOverContent(1);
            BaseFont baseFont = BaseFont.createFont(font.getFile().getAbsolutePath(), "windows-1251", BaseFont.EMBEDDED);
            ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase("Исполнитель: " + mail.getAuthor(), new Font(baseFont,10)), 50, 25, 0);
            pdfStamper.close();
            pdfReader.close();
            return outPdf.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("IO error: " + e.getMessage());
        } catch (TemplateException e) {
            throw new RuntimeException("FreeMarker template error: " + e.getMessage());
        } catch (DocumentException e) {
            throw new RuntimeException("iText document error: " + e.getMessage());
        }
    }
}
