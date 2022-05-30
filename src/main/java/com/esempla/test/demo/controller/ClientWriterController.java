package com.esempla.test.demo.controller;

import com.esempla.test.demo.domain.response.ResponseMessage;
import com.esempla.test.demo.repository.CommentRepository;
import com.esempla.test.demo.service.ClientCommentService;
import com.esempla.test.demo.service.ClientWriterService;
import com.esempla.test.demo.service.FilesStorageService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/file")
public class ClientWriterController {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(ClientWriterController.class);
    private final ClientCommentService clientCommentService;
    private final CommentRepository commentRepository;
    private final ClientWriterService clientWriterService;
    @Autowired
    FilesStorageService storageService;

    public ClientWriterController(ClientCommentService clientCommentService, CommentRepository commentRepository, ClientWriterService clientWriterService) {
        this.clientCommentService = clientCommentService;
        this.commentRepository = commentRepository;
        this.clientWriterService = clientWriterService;
    }


    @GetMapping("/download/csv")
    public ResponseEntity downloadCsv() {
        InputStreamResource file = new InputStreamResource(new ByteArrayInputStream(clientWriterService.csvData()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comments.csv")
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(file);
    }

    @PostMapping("/upload/csv")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            storageService.save(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/download/xml")
    public ResponseEntity downloadXml() {
        log.info("Download data about comments from data base, xml format");
        InputStreamResource file = new InputStreamResource(new ByteArrayInputStream(clientWriterService.xmlData()));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=comments.xml")
                .contentType(MediaType.parseMediaType("application/xml"))
                .body(file);
    }

    @PostMapping("/upload/xml")
    public ResponseEntity<ResponseMessage> uploadFileXml(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            clientWriterService.save(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
}
