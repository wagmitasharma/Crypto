package com.Crypto.serviceImpl;

import com.Crypto.ImageEntity;
import com.Crypto.serviceIntefaces.DecryptionInterface;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class DecryptionImpl implements DecryptionInterface {

    @Override
    public String decodeMsg(MultipartFile image) {
        ImageEntity imageEntity = getPixel(image);
        int i = 0;
        String msg = "";
        while (i!=-1){
            int x = 0, y = 1;
            String bin = "";
            List<ArrayList<Integer>> subPix = imageEntity.getPixels().subList(i, i + 3);
            for (int j =0; j<8; j++){

                bin += subPix.get(x).get(y) %2;
                if (y==3) {
                    y = 1;
                    x += 1;
                }
                else y+=1;
            }
            msg = msg + (char)Integer.parseInt(bin,2);
            if (subPix.get(2).get(3) %2 != 0) i= -1;
            else i+=3;
        }
        return msg;
    }

    @Override
    public ImageEntity getPixel(MultipartFile input) {
        ArrayList<ArrayList<Integer>> pixel = new ArrayList<>();
        ImageEntity imageEntity = new ImageEntity();
        try {
            BufferedImage image = ImageIO.read(input.getInputStream());
            int width = image.getWidth();
            int height = image.getHeight();
            int k;
            imageEntity.setHeight(height);
            imageEntity.setWidth(width);

            for(int i=0; i<imageEntity.getHeight(); i++) {

                for(int j=0; j<imageEntity.getWidth(); j++) {
                    ArrayList<Integer> p = new ArrayList<>();
                    k = image.getRGB(j, i);
                    p.add(0, (k >> 24) & 0xff);
                    p.add(1, (k >> 16) & 0xff);
                    p.add(2, (k >> 8) & 0xff);
                    p.add(3, k & 0xff);
                    pixel.add(p);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        imageEntity.setPixels(pixel);
        return imageEntity;
    }
}
