package ro.itschool.Booking.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import ro.itschool.Booking.entity.Reservation;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class GeneratePdfInvoiceService {
    public void export(HttpServletResponse response, Reservation reservation) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        LocalDate date = LocalDate.now();

        document.open();
        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        Paragraph paragraph = new Paragraph("INVOICE"+ "\n" +
                "---------------------------------------------------\n" +
                "\n" +
                "---------------------------------------------------\n", fontTitle);

        paragraph.setAlignment(Paragraph.ALIGN_CENTER);
        Font fontParagraph = FontFactory.getFont(FontFactory.TIMES);
        fontParagraph.setSize(14);
        UUID uuid = UUID.randomUUID();
        if (reservation.getCoupon() == null) {
            //if coupon is null to have another paragraph without coupon field
        }
        Paragraph paragraph2 = new Paragraph(
                "Date: " + date + "\n" +
                        "Invoice #:" + uuid + "\n" +
                        "Currency: " + "$" + "\n" +
                        "\n" +
                        "Bill To:\n" +
                        reservation.getPerson().getFirstName() + " " + reservation.getPerson().getLastName() + "\n" +
                        reservation.getPerson().getEmail() + "\n" +
                        reservation.getPerson().getMobileNumber() + "\n" +
                        "\n" +
                        "Same as Billing Address\n" +
                        "\n" +
                        "---------------------------------------------------\n"
                        , fontParagraph);

        paragraph2.setAlignment(Paragraph.ALIGN_LEFT);

        Paragraph paragraph3 = new Paragraph("\n" +
                "Accommodation Details:\n" +
                "Property: " + reservation.getProperty().getPropertyName() + "\n" +
                "Check-in: " + reservation.getCheckInDate() + "\n" +
                "Check-out: " + reservation.getCheckOutDate() + "\n" +
                "Country: " + reservation.getCountry() + "\n" +
                "\n" +
                "Description:\n" +
                reservation.getDescription() + "\n" +
                "\n" +
                "Coupon Used: " + reservation.getCoupon() + "                 Subtotal: " + reservation.getPrice() + "\n" +
                "\n" +

                "Total Amount:                        " + reservation.getPrice() + "\n" +
                "\n" +
                "---------------------------------------------------"+
                "\n", fontParagraph);


        paragraph3.setAlignment(Paragraph.ALIGN_LEFT);
        Paragraph paragraph4 = new Paragraph(
                "Please make the payment by the due date to avoid any late charges.\n" +
                "\n" +
                "Thank you for choosing our services!\n" +
                "\n" +
                "---------------------------------------------------\n" +
                "                 " + reservation.getProperty().getPropertyName() + "\n" +
                "           " + reservation.getProperty().getPropertyEmail() + " | " + reservation.getProperty().getPropertyAddress() + "\n" +
                "---------------------------------------------------", fontParagraph);
        paragraph4.setAlignment(Paragraph.ALIGN_BASELINE);
        document.add(paragraph);
        document.add(paragraph2);
        document.add(paragraph3);
        document.add(paragraph4);
        document.close();

    }


}
