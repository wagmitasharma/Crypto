package com.Crypto.serviceIntefaces;

import com.Crypto.ImageEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface DecryptionInterface {
    String decodeMsg(MultipartFile image);

    ImageEntity getPixel(MultipartFile pic);
}
