/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2024. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradelending.documentutils;

import com.kony.dbputilities.fileutil.PDFGenerator;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.temenos.infinity.tradelending.constants.TradeLendingConstants;
import com.temenos.infinity.tradelending.dto.PaymentRequestDTO;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.HTTP_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS;
import static com.temenos.infinity.tradelending.constants.TradeLendingConstants.HTTP_HEADER_CONTENT_DISPOSITION;

/**
 * Transaction report pdf
 *
 * @author k.meiyazhagan
 */
public class TransactionReport {
    private TransactionReport() {
    }

    private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    /**
     * Generate pdf byte [ ].
     *
     * @param inputDto the input dto
     * @return the byte [ ]
     */
    public static byte[] generatePdf(PaymentRequestDTO inputDto) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        try (Document document = new Document(PageSize.A4)) {
            PdfWriter.getInstance(document, os);
            document.open();

            addLogo(document);
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);

            table.addCell(borderHeadingCell("Transaction Details"));
            table.setSpacingBefore(20);
            table.setSpacingAfter(20);
            document.add(table);

            table = new PdfPTable(2);
            table.setWidths(new float[]{1.5f, 2.0f});
            table.setWidthPercentage(100);
            table.addCell(borderKeyCell("Reference Number"));
            table.addCell(borderValueCell(inputDto.getPaymentRequestId()));

            table.addCell(borderKeyCell("From Account"));
            table.addCell(borderValueCell(inputDto.getFromAccount()));

            table.addCell(borderKeyCell("To Account"));
            table.addCell(borderValueCell(inputDto.getToAccount()));

            table.addCell(borderKeyCell("Amount"));
            table.addCell(borderValueCell(inputDto.getAmount()));

            table.addCell(borderKeyCell("Payment Currency"));
            table.addCell(borderValueCell(inputDto.getCurrency()));

            table.addCell(borderKeyCell("Payment Date"));
            table.addCell(borderValueCell(inputDto.getPaymentRequestDate()));

            table.addCell(borderKeyCell("Payment Reference"));
            table.addCell(borderValueCell(inputDto.getPaymentReference()));

            document.add(table);
        } catch (Exception e) {
            alert.prepareError("Error occurred while creating transaction report pdf ", e.getMessage()).log();
        }

        return os.toByteArray();
    }

    /**
     * Gets all response headers.
     *
     * @param paymentRequestId the payment reference
     * @return the all response headers
     */
    public static Map<String, String> getAllResponseHeaders(String paymentRequestId) {
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, TradeLendingConstants.HEADER_APPLICATION_PDF);
        customHeaders.put(HTTP_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS, HTTP_HEADER_CONTENT_DISPOSITION);
        customHeaders.put(HTTP_HEADER_CONTENT_DISPOSITION, "attachment; filename=\"" + paymentRequestId + ".pdf\"");
        return customHeaders;
    }

    /**
     * Border heading cell pdf p cell.
     *
     * @param text the text
     * @return the pdf p cell
     */
    public static PdfPCell borderHeadingCell(String text) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 20, Font.BOLD));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorderColorTop(Color.black);
        cell.setBorderColorBottom(Color.black);
        cell.disableBorderSide(4);
        cell.disableBorderSide(8);
        cell.setBorderWidth(2);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        return cell;
    }

    /**
     * Border key cell pdf p cell.
     *
     * @param text the text
     * @return the pdf p cell
     */
    public static PdfPCell borderKeyCell(String text) {
        Paragraph cellText = new Paragraph(text, new Font(Font.HELVETICA, 10, Font.NORMAL));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setColspan(1);
        cell.setBorderColor(Color.black);
        return cell;
    }

    /**
     * Border value cell pdf p cell.
     *
     * @param text the text
     * @return the pdf p cell
     */
    public static PdfPCell borderValueCell(String text) {
        Paragraph cellText = new Paragraph(StringUtils.isNotBlank(text) ? text : "NA", new Font(Font.HELVETICA, 10, Font.NORMAL));
        PdfPCell cell = new PdfPCell();
        cell.addElement(cellText);
        cell.setBorderColor(Color.black);
        return cell;
    }

    private static void addLogo(Document document) {
        try (InputStream ipStream = PDFGenerator.class.getClassLoader().getResourceAsStream(TradeLendingConstants.INFINITY_LOGO)) {
            Image logo = Image.getInstance(IOUtils.toByteArray(Objects.requireNonNull(ipStream)));
            logo.scaleAbsoluteHeight(30);
            logo.scaleAbsoluteWidth(97);
            document.add(logo);
        } catch (Exception e) {
            alert.prepareError("Error occurred while adding logo to transaction report pdf ", e).log();
        }
    }

}