package com.itextpdf.kernel.pdf;

import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.ExtendedITextTest;
import com.itextpdf.test.annotations.type.IntegrationTest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;

@Category(IntegrationTest.class)
public class PdfDocumentInfoTest extends ExtendedITextTest {

    public static final String sourceFolder = "./src/test/resources/com/itextpdf/kernel/pdf/PdfDocumentInfoTest/";
    public static final String destinationFolder = "./target/test/com/itextpdf/kernel/pdf/PdfDocumentInfoTest/";

    @BeforeClass
    public static void beforeClass() {
        createOrClearDestinationFolder(destinationFolder);
    }

    @Test
    public void documentInfoCreatePdf20() throws IOException, InterruptedException {
        String outFile = destinationFolder + "test01.pdf";
        String cmpFile = sourceFolder + "cmp_test01.pdf";

        PdfDocument document = new PdfDocument(new PdfWriter(outFile, new WriterProperties().setPdfVersion(PdfVersion.PDF_2_0)));
        document.addNewPage();
        document.getDocumentInfo().setAuthor("Alexey");
        document.close();

        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareByContent(outFile, cmpFile, destinationFolder, "diff_"));
        Assert.assertNull(ct.compareDocumentInfo(outFile, cmpFile));
        Assert.assertNull(ct.compareXmp(outFile, cmpFile, true));
    }

    @Test
    public void documentInfoTransformPdf17ToPdf20() throws IOException, InterruptedException {
        String inputFile = sourceFolder + "metadata_pdf.pdf";
        String outFile = destinationFolder + "metadata_pdf_20.pdf";
        String cmpFile = sourceFolder + "cmp_metadata_pdf_20.pdf";

        PdfDocument document = new PdfDocument(new PdfReader(inputFile), new PdfWriter(outFile, new WriterProperties().setPdfVersion(PdfVersion.PDF_2_0)));
        document.close();

        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareByContent(outFile, cmpFile, destinationFolder, "diff_"));
        Assert.assertNull(ct.compareDocumentInfo(outFile, cmpFile));
        Assert.assertNull(ct.compareXmp(outFile, cmpFile, true));
    }

    @Test
    public void changeDocumentVersionAndInfoInAppendMode() throws IOException, InterruptedException {
        String inputFile = sourceFolder + "metadata_pdf.pdf";
        String outFile = destinationFolder + "metadata_pdf_20_append.pdf";
        String cmpFile = sourceFolder + "cmp_metadata_pdf_20_append.pdf";

        PdfDocument document = new PdfDocument(new PdfReader(inputFile),
                new PdfWriter(outFile, new WriterProperties().setPdfVersion(PdfVersion.PDF_2_0)),
                new StampingProperties().useAppendMode());
        document.getDocumentInfo().setAuthor("Alexey Subach");
        document.close();

        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareByContent(outFile, cmpFile, destinationFolder, "diff_"));
        Assert.assertNull(ct.compareDocumentInfo(outFile, cmpFile));
        Assert.assertNull(ct.compareXmp(outFile, cmpFile, true));
    }

    @Test
    public void readInfoFromMetadata() throws IOException {
        String inputFile = sourceFolder + "cmp_metadata_pdf_20.pdf";
        PdfDocument document = new PdfDocument(new PdfReader(inputFile));

        String author = document.getDocumentInfo().getAuthor();
        String subject = document.getDocumentInfo().getSubject();
        String title = document.getDocumentInfo().getTitle();

        document.close();

        Assert.assertEquals("Author", "Bruno Lowagie", author);
        Assert.assertEquals("Title", "Hello World example", title);
        Assert.assertEquals("Subject", "This example shows how to add metadata", subject);
    }

    @Test
    public void changeMetadataInAppendMode() throws IOException, InterruptedException {
        String inputFile = sourceFolder + "cmp_metadata_pdf_20.pdf";
        String outFile = destinationFolder + "metadata_pdf_20_changed_append.pdf";
        String cmpFile = sourceFolder + "cmp_metadata_pdf_20_changed_append.pdf";

        PdfDocument document = new PdfDocument(new PdfReader(inputFile), new PdfWriter(outFile), new StampingProperties().useAppendMode());
        document.getDocumentInfo().setAuthor("Alexey Subach");
        document.close();

        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareByContent(outFile, cmpFile, destinationFolder, "diff_"));
        Assert.assertNull(ct.compareDocumentInfo(outFile, cmpFile));
        Assert.assertNull(ct.compareXmp(outFile, cmpFile, true));
    }

    @Test
    public void simpleStampingMetadataLeaveUnchanged() throws IOException, InterruptedException {
        String inputFile = sourceFolder + "cmp_metadata_pdf_20_changed_append.pdf";
        String outFile = destinationFolder + "metadata_pdf_20_unchanged_stamper.pdf";
        String cmpFile = sourceFolder + "cmp_metadata_pdf_20_changed_append.pdf";

        PdfDocument document = new PdfDocument(new PdfReader(inputFile), new PdfWriter(outFile), new StampingProperties());
        String author = document.getDocumentInfo().getAuthor();
        document.close();

        Assert.assertEquals("Author", "Bruno Lowagie; Alexey Subach", author);

        CompareTool ct = new CompareTool();
        Assert.assertNull(ct.compareByContent(outFile, cmpFile, destinationFolder, "diff_"));
        Assert.assertNull(ct.compareDocumentInfo(outFile, cmpFile));
        Assert.assertNull(ct.compareXmp(outFile, cmpFile, true));
    }

}