package com.Crypto.serviceIntefaces;

import com.Crypto.ImageEntity;

import java.io.File;
import java.util.ArrayList;

public interface EncryptionInterface {
    String encrypt(String msg);

    ArrayList<ArrayList<Integer>> modPix(ImageEntity imageEntity, String data);

    String makeEncodedImage(ImageEntity imageEntity, int msgLength);

    ImageEntity getPixel(String pic);

    ArrayList<char[]> gendata(String data);

}
