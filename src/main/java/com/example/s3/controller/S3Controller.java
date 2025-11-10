package com.example.s3.controller;

import com.example.s3.service.S3Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class S3Controller {

    private final S3Service s3Service;

    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("files", s3Service.listFiles());
        return "index";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        s3Service.uploadFile(file);
        model.addAttribute("message", "File uploaded successfully!");
        model.addAttribute("files", s3Service.listFiles());
        return "index";
    }

    @GetMapping("/download/{filename}")
    public void downloadFile(@PathVariable String filename, HttpServletResponse response) throws IOException {
        byte[] data = s3Service.downloadFile(filename);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.getOutputStream().write(data);
        response.flushBuffer();
    }

    @GetMapping("/delete/{filename}")
    public String deleteFile(@PathVariable String filename, Model model) {
        s3Service.deleteFile(filename);
        model.addAttribute("message", "File deleted successfully!");
        model.addAttribute("files", s3Service.listFiles());
        return "index";
    }
}
