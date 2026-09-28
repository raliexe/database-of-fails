package com.backend.service.pdf;

import com.backend.entity.Fail;
import com.backend.entity.FailImage;
import com.backend.entity.Pdf;
import com.backend.exception.NotFoundException;
import com.backend.exception.PdfException;
import com.backend.exception.PersistenceException;
import com.backend.repository.PdfRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfServiceImpl implements PdfService {

    private final Font font12 = FontFactory.getFont(FontFactory.HELVETICA, 12);
    private final Font font10 = FontFactory.getFont(FontFactory.HELVETICA, 10);
    private final Font font26B = FontFactory.getFont(FontFactory.HELVETICA, 26, Font.BOLD);

    private final PdfRepository pdfRepository;

    @Override
    public byte[] findPdfByFailId(Long failId) {
        try {
            Pdf pdf = pdfRepository.findPdfByFailId(failId);
            if (pdf != null) {
                return pdf.getContent();
            }
            throw new NotFoundException("Could not find PDF!");
        } catch (DataAccessException e) {
            throw new PersistenceException("A server error occured", e);
        }

    }

    @Override
    public Pdf createPdfForFail(Fail fail) {
        try {
            if (fail == null) {
                throw new NotFoundException("Could not find fail!");
            }
            Pdf pdf = new Pdf();
            byte[] pdfContent = generatePdf(fail);
            pdf.setContent(pdfContent);
            pdf.setFail(fail);
            return pdfRepository.save(pdf);
        } catch (DataAccessException e) {
            log.debug("DataAccessException occured", e);
            throw new PersistenceException("A server error occured", e);
        }
    }

    private byte[] generatePdf(Fail fail) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            PdfPTable headerTable = generateHeaderTable(fail);
            document.add(headerTable);

            PdfPTable contentTable = generateContentTable(fail);
            document.add(contentTable);

            document.close();
            return out.toByteArray();
        } catch (DocumentException | IOException e) {
            throw new PdfException("Pdf couldn't be generated");
        }
    }

    private PdfPTable generateHeaderTable(Fail fail) throws DocumentException, IOException {
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new int[]{12, 1, 12});

        FailImage mainImage = new FailImage();
        for (FailImage img : fail.getImages()) {
            if (img.getIsMain()) {
                mainImage = img;
            }
        }
        addFilledCellWithoutBorderImage(headerTable, mainImage, Element.ALIGN_LEFT);

        addEmptyCell(headerTable);

        String formattedDate = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(fail.getDate());
        addFilledCellWithoutBorderPhraseChunks(
                headerTable, fail.getName(), formattedDate, fail.getUser().getNickname(),
                font26B, font10, font12, Element.ALIGN_LEFT);

        headerTable.setSpacingAfter(20);

        return headerTable;
    }

    private PdfPTable generateContentTable(Fail fail) throws DocumentException {
        PdfPTable contentTable = new PdfPTable(1);
        contentTable.setWidthPercentage(100);
        contentTable.setWidths(new int[]{1});

        addFilledCellWithoutBorder(contentTable, fail.getDescription(), font12, Element.ALIGN_LEFT);
        contentTable.setSpacingAfter(10);

        return contentTable;
    }

    private void addFilledCellWithoutBorderImage(PdfPTable table, FailImage image, int alignH)
            throws BadElementException, IOException {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(alignH);
        Image img = Image.getInstance(image.getContent());
        img.setBorderColor(BaseColor.WHITE);
        table.addCell(img);
    }

    private void addFilledCellWithoutBorderPhraseChunks(
            PdfPTable table, String failName, String formattedDate, String userName,
            Font font1, Font font2, Font font3, int alignH) {
        Phrase p = new Phrase();
        p.add(new Chunk(failName,  font1));
        p.add(new Chunk("\n\n\n" + formattedDate, font2));
        p.add(new Chunk("\n\nby " + userName, font3));
        PdfPCell cell = new PdfPCell(p);
        cell.setHorizontalAlignment(alignH);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
    }

    private void addFilledCellWithoutBorder(PdfPTable table, String phrase, Font font, int alignH) {
        PdfPCell cell = new PdfPCell(new Phrase(phrase, font));
        cell.setHorizontalAlignment(alignH);
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
    }

    private void addEmptyCell(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(cell);
    }
}