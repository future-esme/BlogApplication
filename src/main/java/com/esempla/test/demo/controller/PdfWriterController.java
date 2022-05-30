package com.esempla.test.demo.controller;

import com.esempla.test.demo.service.PdfWriterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class PdfWriterController {
    private final Logger log = LoggerFactory.getLogger(PdfWriterController.class);
    public final PdfWriterService pdfWriterService;

    public PdfWriterController(PdfWriterService pdfWriterService) {
        this.pdfWriterService = pdfWriterService;
    }

    @GetMapping(value = "/pdf/{id}")
    public ResponseEntity<InputStreamResource> getPostByIdAndComments(@PathVariable Long id) {
        if (pdfWriterService.employeePDFReport(id) == null) {
            return ResponseEntity.notFound().build();
        } else {
            ByteArrayInputStream bis = pdfWriterService.employeePDFReport(id);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "inline; filename=posts.pdf");

            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        }
    }

    @GetMapping(value = "pdf/comments")
    public ResponseEntity<InputStreamResource> getAllComments() {
        ByteArrayInputStream bis = pdfWriterService.allComments();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=posts.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}
