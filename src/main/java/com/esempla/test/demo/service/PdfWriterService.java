package com.esempla.test.demo.service;

import com.esempla.test.demo.domain.Comment;
import com.esempla.test.demo.domain.Post;
import com.esempla.test.demo.domain.Storage;
import com.esempla.test.demo.repository.CommentRepository;
import com.esempla.test.demo.repository.PostRepository;
import com.esempla.test.demo.repository.StorageRepository;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.stream.Stream;

@Service
public class PdfWriterService {
    private final Logger log = LoggerFactory.getLogger(PdfWriterService.class);
    public static final String IMAGE = "src/main/resources/template1.jpg";
    private final StorageRepository storageRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private BaseColor primaryColor;
    private BaseColor secondaryColor;

    public PdfWriterService(StorageRepository storageRepository, PostRepository postRepository, CommentRepository commentRepository) {
        this.storageRepository = storageRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public void initializeColors() {
        primaryColor = new BaseColor(140, 206, 177);
        secondaryColor = new BaseColor(0, 49, 56);
    }

    public String styleTime(Instant time){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
        LocalDateTime ldt = LocalDateTime.ofInstant(time, ZoneOffset.UTC);
        return dateTimeFormatter.format(ldt);
    }

    public ByteArrayInputStream employeePDFReport (Long id){
        if(!postRepository.findById(id).isPresent()) {
            return null;
        }
            Post post = postRepository.findById(id).get();
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            initializeColors();
            try {
                PdfWriter writer = PdfWriter.getInstance(document, out);
                document.open();

                // Add Text to PDF file ->
                Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
                Font font2 = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.WHITE);
                Paragraph title = new Paragraph(post.getTitle(), font2);
                Paragraph content = new Paragraph(post.getContent(), font);
                Paragraph createTime = new Paragraph(styleTime(post.getCreateTime()), font);
                Paragraph user = new Paragraph(post.getUser().getUsername(), font);
                title.setAlignment(Element.ALIGN_CENTER);
                content.setAlignment(Element.ALIGN_JUSTIFIED);
                createTime.setAlignment(Element.ALIGN_RIGHT);
                user.setAlignment(Element.ALIGN_LEFT);
                document.add(user);
                document.add(createTime);
                document.add(Chunk.NEWLINE);
                document.add(title);
                document.add(Chunk.NEWLINE);
                document.add(content);
                document.add(Chunk.NEWLINE);
                /*PdfContentByte contentByte = writer.getDirectContentUnder();
                Rectangle rectangle = new Rectangle(100, 150, 220, 200);
                ColumnText.showTextAligned(contentByte, Element.ALIGN_JUSTIFIED, content,  (rectangle.getLeft() + rectangle.getRight()) / 2, rectangle.getBottom()+ 100, 0);
                contentByte.saveState();
                contentByte.setColorStroke(BaseColor.BLUE);
                contentByte.rectangle(rectangle.getLeft(), rectangle.getBottom(), rectangle.getWidth(), rectangle.getHeight());
                contentByte.stroke();
                contentByte.restoreState();*/
                PdfContentByte canvas = writer.getDirectContentUnder();

                byte[] imagebytes = DatatypeConverter.parseBase64Binary(storageRepository.findStorageByName("template").getContent());

                Image image = Image.getInstance(imagebytes);
                //Image image = Image.getInstance("template1.jpg");

                // Storage storage = storageRepository.findStorageByName("template1");
                //Image image = Image.getInstance(storage.getContent());


                /*byte[] fileContent = Files.readAllBytes(Path.of(IMAGE));
                Storage storage = new Storage();
                storage.setName("template1");
                storage.setContent(fileContent);
                storageRepository.save(storage);*/

                image.scaleAbsolute(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                image.setAbsolutePosition(0, 0);
                canvas.addImage(image);
                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                // Add PDF Table Header ->
                Stream.of("Content", "Create Time", "Author").forEach(headerTitle ->
                {
                    PdfPCell header = new PdfPCell();
                    Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
                    header.setBackgroundColor(secondaryColor);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(headerTitle, headFont));
                    table.addCell(header);
                });
                List<Comment> comments = commentRepository.findAllByPostId(post);
                for (Comment comment : comments){
                    PdfPCell contentCell = new PdfPCell(new Phrase(comment.getContent()));
                    contentCell.setPaddingLeft(4);
                    contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(contentCell);

                    PdfPCell createTimeCell = new PdfPCell(new Phrase(styleTime(comment.getCreateTime())));
                    createTimeCell.setPaddingLeft(4);
                    createTimeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    createTimeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(createTimeCell);

                    PdfPCell authorCell = new PdfPCell(new Phrase(comment.getUser().getUsername()));
                    authorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    authorCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    authorCell.setPaddingRight(4);
                    table.addCell(authorCell);
                }
                document.add(table);

                document.close();
            } catch (DocumentException e) {
                log.error(e.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        return new ByteArrayInputStream(out.toByteArray());
    }
    public ByteArrayInputStream allComments (){
        List<Comment> comments = commentRepository.findAll();
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            PdfContentByte canvas = writer.getDirectContentUnder();

            byte[] imagebytes = DatatypeConverter.parseBase64Binary(storageRepository.findStorageByName("template2").getContent());

            Image image = Image.getInstance(imagebytes);

            // Storage storage = storageRepository.findStorageByName("template1");
            //Image image = Image.getInstance(storage.getContent());


                /*byte[] fileContent = Files.readAllBytes(Path.of(IMAGE));
                Storage storage = new Storage();
                storage.setName("template1");
                storage.setContent(fileContent);
                storageRepository.save(storage);*/

            image.scaleAbsolute(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            image.setAbsolutePosition(0, 0);
            canvas.addImage(image);
            PdfPTable table = new PdfPTable(3);
            // Add PDF Table Header ->
            Stream.of("Content", "Create Time", "Author").forEach(headerTitle ->
            {
                PdfPCell header = new PdfPCell();
                Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);
                header.setBackgroundColor(secondaryColor);
                header.setHorizontalAlignment(Element.ALIGN_CENTER);
                header.setBorderWidth(2);
                header.setPhrase(new Phrase(headerTitle, headFont));
                table.addCell(header);
            });
            for (Comment comment : comments){
                PdfPCell contentCell = new PdfPCell(new Phrase(comment.getContent()));
                contentCell.setPaddingLeft(4);
                contentCell.setBackgroundColor(BaseColor.WHITE);
                contentCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                contentCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(contentCell);

                PdfPCell createTimeCell = new PdfPCell(new Phrase(styleTime(comment.getCreateTime())));
                createTimeCell.setPaddingLeft(4);
                createTimeCell.setBackgroundColor(BaseColor.WHITE);
                createTimeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                createTimeCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(createTimeCell);

                PdfPCell authorCell = new PdfPCell(new Phrase(comment.getUser().getUsername()));
                authorCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                authorCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                authorCell.setBackgroundColor(BaseColor.WHITE);
                authorCell.setPaddingRight(4);
                table.addCell(authorCell);
            }
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            document.add(table);

            document.close();
        } catch (DocumentException e) {
            log.error(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
