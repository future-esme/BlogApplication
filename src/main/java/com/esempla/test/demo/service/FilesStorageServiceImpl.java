package com.esempla.test.demo.service;

import com.esempla.test.demo.controller.PostController;
import com.esempla.test.demo.domain.Comment;
import com.esempla.test.demo.repository.CommentRepository;
import com.esempla.test.demo.repository.PostRepository;
import com.esempla.test.demo.repository.UserRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
    private final Path root = Paths.get("uploads");
    private final Logger log = LoggerFactory.getLogger(PostController.class);

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public FilesStorageServiceImpl(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
            updateDb(file);
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

    }

    @Override
    public UrlResource load(String filename) {
        try {
            Path file = root.resolve(filename);
            UrlResource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }

    @Override
    public void updateDb(MultipartFile file) {
        try {
            CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build();
            String[] nextRecord;
            while ((nextRecord = csvReader.readNext()) != null) {
                Comment comment = new Comment(
                        parseLong(nextRecord[0]),
                        nextRecord[1],
                        Instant.parse(nextRecord[2]),
                        postRepository.findById(parseLong(nextRecord[3])).get(),
                        userRepository.findById(parseLong(nextRecord[4])).get()
                );
                commentRepository.save(comment);
            }
            csvReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
