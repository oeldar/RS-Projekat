package grupa5;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;

import grupa5.baza_podataka.Kupovina;

import com.itextpdf.io.font.constants.StandardFonts;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

public class PdfGenerator {

    public static void generatePdf(File pdfFile, Kupovina kupovina) {
        try {
            // Kreiraj PdfWriter instance
            PdfWriter writer = new PdfWriter(pdfFile);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, new PageSize(900, 300));
            
            // Učitaj font koji podržava specijalne karaktere
            PdfFont font = PdfFontFactory.createFont("src/main/resources/grupa5/fonts/Montserrat-Regular.ttf", PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);
            
            float fontSize = 20f;
            document.setMargins(0, 0, 0, 0);
    
            // Kreiraj tabelu za sliku i podatke
            Table table = new Table(UnitValue.createPercentArray(new float[]{2, 3, 1}));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setBorder(Border.NO_BORDER);
    
            // Dodaj sliku događaja
            String eventImagePath = "src/main/resources/grupa5/" + kupovina.getDogadjaj().getPutanjaDoSlike();
            // System.out.println(eventImagePath);
            // System.out.println("Postoji putanja do slike: " + new File(eventImagePath).exists());
            if (eventImagePath == null || !new File(eventImagePath).exists()) {
                eventImagePath = "src/main/resources/grupa5/assets/events_photos/default-event.png";
            }

            File croppedImageFile = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "croppedEventImage.png");
            ImageUtils.cropImageToSquare(new File(eventImagePath), croppedImageFile);

            Image eventImage = new Image(ImageDataFactory.create(croppedImageFile.getAbsolutePath()));
            eventImage.setAutoScale(true);
            eventImage.setWidth(UnitValue.createPointValue(150)); // Širina slike
            eventImage.setHeight(UnitValue.createPointValue(150)); // Visina slike
            
            // Kreiraj praznu ćeliju da biste dodali razmak između slike i podataka
            Cell imageCell = new Cell().add(eventImage);
            imageCell.setBorder(Border.NO_BORDER);
            imageCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            table.addCell(imageCell);
    
            // Dodaj informacije o kupovini u tabelu
            String eventName = kupovina.getDogadjaj().getNaziv();
            String data = String.format(
                "Kupac: %s %s\n" +
                "Lokacija: %s, %s\n" +
                "Događaj: %s\n" +
                "Sektor: %s\n" +
                "Broj karata: %d\n" +
                "Cijena: %.2f\n",
                kupovina.getKorisnik().getIme(),
                kupovina.getKorisnik().getPrezime(),
                kupovina.getDogadjaj().getMjesto().getNaziv(),
                kupovina.getDogadjaj().getLokacija().getNaziv(),
                eventName,
                kupovina.getKarta().getSektorNaziv(),
                kupovina.getBrojKarata(),
                kupovina.getKonacnaCijena()
            );
            Paragraph dataParagraph = new Paragraph(data).setFont(font).setFontSize(fontSize);
            
            // Kreiraj praznu ćeliju sa dodatnim razmakom između slika i podataka
            Cell dataCell = new Cell().add(dataParagraph);
            dataCell.setBorder(Border.NO_BORDER);
            dataCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            dataCell.setPaddingLeft(10); // Povećava razmak unutar ćelije
            table.addCell(dataCell);
    
            // Dodaj QR kod
            String qrData = generateQrData(kupovina);
            BufferedImage qrImage = generateQrCode(qrData);
            File qrCodeFile = new File(System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "tempQrCode.png");
            ImageIO.write(qrImage, "png", qrCodeFile);
            Image qrCode = new Image(ImageDataFactory.create(qrCodeFile.getAbsolutePath()));
            qrCode.setAutoScale(true);
            qrCode.setWidth(UnitValue.createPointValue(100)); // Širina QR koda
            qrCode.setHeight(UnitValue.createPointValue(100)); // Visina QR koda
    
            // Kreiraj ćeliju za QR kod
            Cell qrCell = new Cell().add(qrCode);
            qrCell.setBorder(Border.NO_BORDER);
            qrCell.setVerticalAlignment(VerticalAlignment.BOTTOM);
            qrCell.setPaddingLeft(10); // Povećava razmak unutar ćelije
            table.addCell(qrCell);
    
            // Dodaj tabelu u dokument
            document.add(table);
    
            // Zatvori dokument
            document.close();
            System.out.println("PDF uspešno kreiran: " + pdfFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Greška pri kreiranju PDF-a: " + e.getMessage());
            e.printStackTrace();
        }
    }    


    private static BufferedImage generateQrCode(String data) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300, Map.of(EncodeHintType.CHARACTER_SET, "UTF-8"));
        BufferedImage bufferedImage = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 300; x++) {
            for (int y = 0; y < 300; y++) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return bufferedImage;
    }

    private static String generateQrData(Kupovina kupovina) {
        StringBuilder qrData = new StringBuilder();
    
        qrData.append("ID: ").append(kupovina.getKupovinaID()).append("\n");
    
        if (kupovina.getKorisnik() != null) {
            qrData.append("Kupac: ").append(kupovina.getKorisnik().getIme()).append(" ").append(kupovina.getKorisnik().getPrezime()).append("\n");
        }
    
        if (kupovina.getDogadjaj() != null) {
            qrData.append("Događaj: ").append(kupovina.getDogadjaj().getNaziv()).append("\n");
    
            if (kupovina.getDogadjaj().getMjesto() != null) {
                qrData.append("Mjesto: ").append(kupovina.getDogadjaj().getMjesto().getNaziv()).append("\n");
            }
    
            if (kupovina.getDogadjaj().getPocetakDogadjaja() != null) {
                qrData.append("Datum i vrijeme održavanja: ").append(kupovina.getDogadjaj().getPocetakDogadjaja()).append("\n");
            }
        }
    
        if (kupovina.getKonacnaCijena() != null) {
            qrData.append("Cijena: ").append(kupovina.getKonacnaCijena()).append("\n");
        }
    
        return qrData.toString();
    }
    
}
