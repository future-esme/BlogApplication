package com.esempla.test.demo.service;

import com.esempla.test.demo.repository.PostRepository;
import com.esempla.test.demo.repository.UserRepository;
import org.slf4j.Logger;
import com.esempla.test.demo.domain.Comment;
import com.esempla.test.demo.repository.CommentRepository;
import com.esempla.test.demo.service.dto.CommentCsvDto;
import com.esempla.test.demo.service.dto.CommentDtoXml;
import com.esempla.test.demo.service.dto.CommentsDtoXml;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

@Service
public class ClientWriterService {

    private final Logger log = LoggerFactory.getLogger(ClientWriterService.class);
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CustomMappingStrategy<CommentCsvDto> mappingStrategy;

    public ClientWriterService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository, CustomMappingStrategy<CommentCsvDto> mappingStrategy) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.mappingStrategy = mappingStrategy;
    }

    public byte[] csvData() {
        List<Comment> comments = commentRepository.findAll();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (Writer out = new BufferedWriter(new OutputStreamWriter(baos))) {
                StatefulBeanToCsv<CommentCsvDto> writer = new StatefulBeanToCsvBuilder<CommentCsvDto>(out)
                        .withSeparator(';')
                        .withQuotechar(CSVWriter.NO_ESCAPE_CHARACTER)
                        .withEscapechar(CSVWriter.NO_ESCAPE_CHARACTER)
                        .withOrderedResults(false)
                        .build();
                List<CommentCsvDto> commentCsvDto = new ArrayList<>();
                for (Comment comment : comments) {
                    commentCsvDto.add(new CommentCsvDto(
                            comment.getId(),
                            comment.getContent(),
                            comment.getCreateTime(),
                            comment.getUser().getId(),
                            comment.getPostId().getId()));
                }
                writer.write(commentCsvDto);
            }
            return baos.toByteArray();
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.info(e.getMessage(), e);
        }
        return null;
    }
    public byte[] xmlData(){
        List<Comment> comments = commentRepository.findAll();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                List<CommentDtoXml> commentDtoXml = new ArrayList<>();
                for (Comment comment : comments) {
                    commentDtoXml.add(new CommentDtoXml(
                            comment.getId(),
                            comment.getContent(),
                            DatatypeFactory.newInstance().newXMLGregorianCalendar(String.valueOf(comment.getCreateTime())),
                            comment.getPostId().getId(),
                            comment.getUser().getId()
                    ));
                }
                JAXBContext jaxbContext = JAXBContext.newInstance(CommentsDtoXml.class);
                CommentsDtoXml commentsDtoXml = new CommentsDtoXml();
                commentsDtoXml.setCommentDtoXmlList(commentDtoXml);
                Marshaller marshaller = jaxbContext.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                StringWriter xmlWriter = new StringWriter();
                marshaller.marshal(commentsDtoXml, xmlWriter);
                baos.write(xmlWriter.toString().getBytes(StandardCharsets.UTF_8));
                return baos.toByteArray();
        } catch (IOException | JAXBException | DatatypeConfigurationException e) {
            log.info(e.getMessage(), e);
        }
        return null;
    }

    public void save(MultipartFile inputFile) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(CommentsDtoXml.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        try {
            CommentsDtoXml commentsDtoXmlList = (CommentsDtoXml) jaxbUnmarshaller.unmarshal(new InputStreamReader(inputFile.getInputStream()));
            for (CommentDtoXml commentDtoXml : commentsDtoXmlList.getCommentDtoXmlList()) {
                commentRepository.save(new Comment(
                        commentDtoXml.getContent(),
                        commentDtoXml.getCreateTime().toGregorianCalendar().toInstant(),
                        postRepository.findById(commentDtoXml.getPostId()).get(),
                        userRepository.findById(commentDtoXml.getUserId()).get()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


    /*    List<CommentDtoString> commentCsvDto = new ArrayList<>();
        for (Comment comment : comments) {
            commentCsvDto.add(new CommentDtoString(
                    comment.getId().toString(),
                    comment.getContent(),
                    comment.getCreateTime().toString(),
                    comment.getUser().getId().toString(),
                    comment.getPostId().getId().toString()));
        }
        String information = new String();
        for (CommentDtoString commentDtoString : commentCsvDto) {
            information += commentDtoString.getId() + ";";
            information += commentDtoString.getContent() + ";";
            information += commentDtoString.getCreateTime() + ";";
            information += commentDtoString.getPostId() + ";";
            information += commentDtoString.getUserId() + "\n";
        }
        return new ByteArrayInputStream(information.getBytes(StandardCharsets.UTF_8));
    }*/
/*

    public void writeAndSaveFile(Writer writerSave) {
        //var comments = List.of(clientCommentService.getComments());
        var comments = commentRepository.findAll();
        mappingStrategy.setType(CommentCsvDto.class);
        try {
            StatefulBeanToCsv<CommentCsvDto> beanToCsv = new StatefulBeanToCsvBuilder<CommentCsvDto>(writerSave)
                    .withSeparator(';')
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withMappingStrategy(mappingStrategy)
                    .build();
            List<CommentCsvDto> commentCsvDto = new ArrayList<>();
            for (Comment comment : comments) {
                commentCsvDto.add(new CommentCsvDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreateTime(),
                        comment.getUser().getId(),
                        comment.getPostId().getId()));
            }
            beanToCsv.write(commentCsvDto);

        } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException ex) {
            Logger.getLogger(ClientWriterController.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    */
/*
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (Writer out = new BufferedWriter(new OutputStreamWriter(baos))) {
                StatefulBeanToCsv<T> writer = new StatefulBeanToCsvBuilder<T>(out)
                    .withMappingStrategy(new HeaderColumnNameAndOrderMappingStrategy<T>(persistentClass))
                    .withQuotechar(CSVWriter.NO_ESCAPE_CHARACTER)
                    .withEscapechar(CSVWriter.NO_ESCAPE_CHARACTER)
                    .withSeparator(';')
                    .withOrderedResults(false)
                    .build();
                writer.write(events);
            }
            return baos.toByteArray();
        }
*/
