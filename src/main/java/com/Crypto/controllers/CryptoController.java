package com.Crypto.controllers;

import com.Crypto.serviceIntefaces.DecryptionInterface;
import com.Crypto.serviceIntefaces.EncryptionInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    @Autowired
    private EncryptionInterface encryptionInterface;

    @Autowired
    private DecryptionInterface decryptionInterface;

//    @GetMapping(value = "/encrypt", produces = MediaType.IMAGE_PNG_VALUE)
//    public ResponseEntity<byte[]> getEncryptImage(@RequestParam String msg) throws IOException {
//        File encyptedImage = new File(encryptionInterface.encrypt(msg));
//        InputStream in = new FileInputStream(encyptedImage);
//        byte[] imgBytes = StreamUtils.copyToByteArray(in);
//        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imgBytes);
//    }

    @GetMapping(value = "/encrypt", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getEncryptImage(@RequestParam String msg) throws IOException {
        File encyptedImage = new File(encryptionInterface.encrypt(msg));
        InputStream in = new FileInputStream(encyptedImage);
        byte[] imgBytes = StreamUtils.copyToByteArray(in);
        return new ResponseEntity<>(imgBytes,HttpStatus.OK);
    }

    @GetMapping(path = "/download")
    public ResponseEntity<Resource> download(@RequestParam String image) throws IOException {
        File file = new File(image);
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=encryptedImg.png");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @PostMapping(path = "/decrypt")
    public ResponseEntity<String> decodeMsg(@RequestParam MultipartFile image){
        String msg = decryptionInterface.decodeMsg(image);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
