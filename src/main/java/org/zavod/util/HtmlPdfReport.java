package org.zavod.util;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class HtmlPdfReport implements IPdfReport<MailEntity> {

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public byte[] create(MailEntity mail) {

        final Resource font = resourceLoader.getResource("classpath:static/fonts/times.ttf");
        final Resource imagesPath = resourceLoader.getResource("classpath:static/images");
        final Resource templateFolder = resourceLoader.getResource("classpath:templates/freemarker");

        try (StringWriter htmlWriter = new StringWriter(); ByteArrayOutputStream outPdf = new ByteArrayOutputStream()) {

            /* FreeMarker */

            // Config
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);
            TemplateLoader templateLoader = new FileTemplateLoader(templateFolder.getFile());
            cfg.setTemplateLoader(templateLoader);
            cfg.setDefaultEncoding("UTF-8");
            // Model
            Map<String, Object> modelMap = new HashMap<>();
            modelMap.put("mailNumber", mail.getMailNumber());
            modelMap.put("mailDate", mail.getCreateDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            modelMap.put("mailRecipient", mail.getMailRecipient());
            modelMap.put("mailTitle", mail.getMailTitle());
            modelMap.put("mailText", mail.getMailText());
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

            // 1251 - times.ttf encoding
            XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(htmlWriter.toString().getBytes()),
                    null, Charset.forName("windows-1251"), xmlWorkerFontProvider, imagesPath.getFile().getAbsolutePath());

            document.close();

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
