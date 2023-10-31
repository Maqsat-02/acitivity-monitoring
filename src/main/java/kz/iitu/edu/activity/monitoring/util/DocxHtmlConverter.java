package kz.iitu.edu.activity.monitoring.util;

import jakarta.xml.bind.JAXBException;
import org.docx4j.Docx4J;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.convert.out.ConversionFeatures;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.convert.out.html.SdtToListSdtTagHandler;
import org.docx4j.convert.out.html.SdtWriter;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class DocxHtmlConverter {
    public String docxToHtml(InputStream docxInputStream) throws Docx4JException {
        ByteArrayOutputStream htmlOutputStream = new ByteArrayOutputStream();
        docxToHtml(docxInputStream, htmlOutputStream);
        return htmlOutputStream.toString(StandardCharsets.UTF_8);
    }

    private void docxToHtml(InputStream docxInputStream, OutputStream htmlOutputStream) throws Docx4JException {
        WordprocessingMLPackage docxIn = WordprocessingMLPackage.load(docxInputStream);

        HTMLSettings htmlSettings = new HTMLSettings();
        htmlSettings.setOpcPackage(docxIn);

        // list numbering:  depending on whether you want list numbering hardcoded, or done using <li>.
        // see https://github.com/plutext/docx4j-ImportXHTML/blob/master/src/samples/java/org/docx4j/samples/DocxToXhtmlAndBack.java
        boolean nestLists = true;
        if (nestLists) {
            SdtWriter.registerTagHandler("HTML_ELEMENT", new SdtToListSdtTagHandler());
        } else {
            htmlSettings.getFeatures().remove(ConversionFeatures.PP_HTML_COLLECT_LISTS);
        } // must do one or the other

        Docx4J.toHTML(htmlSettings, htmlOutputStream, Docx4J.FLAG_EXPORT_PREFER_XSL);
    }

    public OutputStream htmlToDocx(String html) throws Docx4JException, JAXBException {
        InputStream htmlInputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream docxOutputStream = new ByteArrayOutputStream();
        htmlToDocx(htmlInputStream, docxOutputStream);
        return docxOutputStream;
    }

    private void htmlToDocx(InputStream htmlInputStream, OutputStream docxOutputStream) throws Docx4JException, JAXBException {
        WordprocessingMLPackage docxOut = WordprocessingMLPackage.createPackage();
        NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
        docxOut.getMainDocumentPart().addTargetPart(ndp);
        ndp.unmarshalDefaultNumbering();

        XHTMLImporterImpl XHTMLImporter = new XHTMLImporterImpl(docxOut);
        XHTMLImporter.setHyperlinkStyle("Hyperlink");
        docxOut.getMainDocumentPart().getContent().addAll(
                XHTMLImporter.convert(htmlInputStream, null)
        );

        Docx4J.save(docxOut, docxOutputStream);
    }
}
