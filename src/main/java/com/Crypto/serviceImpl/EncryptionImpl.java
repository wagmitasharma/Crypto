package com.Crypto.serviceImpl;

import com.Crypto.ImageEntity;
import com.Crypto.serviceIntefaces.EncryptionInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EncryptionImpl implements EncryptionInterface {
    @Override
    public String encrypt(String msg) {
    String picture = "LiloStichSample.jpg";
    ImageEntity imageEntity = getPixel(picture);
    imageEntity.setPixels(modPix(imageEntity, msg));
    System.out.println("All ok");
    return makeEncodedImage(imageEntity,msg.length()*3);
    }

    @Override
    public ArrayList<ArrayList<Integer>> modPix(ImageEntity imageEntity, String data){
        ArrayList<char[]> dataList = gendata(data);
        ArrayList<ArrayList<Integer>> newPix = new ArrayList<>();
        List<ArrayList<Integer>> newSubPix;
        int k=0;
            for (int i=0; i<dataList.size(); i++) {
            newSubPix = imageEntity.getPixels().subList(k,k+3);
//            System.out.println(newSubPix);
            int x =0, y=1;
            for (int j=0; j<8; j++) {
                if (dataList.get(i)[j] == '1' && newSubPix.get(x).get(y) %2 ==0 ) newSubPix.get(x).set(y, newSubPix.get(x).get(y)-1);
                else if (dataList.get(i)[j] == '0' && newSubPix.get(x).get(y) %2 !=0 ) newSubPix.get(x).set(y, newSubPix.get(x).get(y)-1);
                if (i != dataList.size() -1 && j==7 && newSubPix.get(x).get(y+1) %2 !=0 ) newSubPix.get(x).set(y+1, newSubPix.get(x).get(y+1)-1);
                else if (i==dataList.size()-1 && j==7 && newSubPix.get(x).get(y+1) %2 ==0 ) newSubPix.get(x).set(y+1, newSubPix.get(x).get(y+1)-1);
                if (y==3) {
                    y = 1;
                    x += 1;
                }
                else y+=1;
            }
            newPix.addAll(newSubPix);
            k+=3;
        }
            newPix.addAll(imageEntity.getPixels().subList(newPix.size(),imageEntity.getPixels().size()));
            imageEntity.setPixels(newPix);
            return newPix;
    }

    @Override
    public String makeEncodedImage(ImageEntity imageEntity, int msgLength){
        int p;
        int k =0;
        int flag = 0;
        File output = new File(imageEntity.getName().substring(0,imageEntity.getName().length()-4) + "Encoded.png");
        try {
            BufferedImage img = ImageIO.read(new File (imageEntity.getName()));
            BufferedImage newImg = new BufferedImage(img.getWidth(),img.getHeight(), img.getType());
            newImg.getGraphics().drawImage(img,0,0,null);
            for (int i = 0; i < newImg.getHeight(); i++) {
                for (int j = 0; j < newImg.getWidth(); j++) {
                    p = (imageEntity.getPixels().get(k).get(0) << 24) | (imageEntity.getPixels().get(k).get(1) << 16)
                            | (imageEntity.getPixels().get(k).get(2) << 8) | imageEntity.getPixels().get(k).get(3);
                    k += 1;
                    newImg.setRGB(j, i, p);
                    msgLength--;
                    if (msgLength == 0) { flag=1; break;}
                }
                if (flag==1) break;
            }
            ImageIO.write(newImg,"png",output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output.getPath();
    }

    @Override
    public ImageEntity getPixel(String pic) {
        ArrayList<ArrayList<Integer>> pixel = new ArrayList<>();
        ImageEntity imageEntity = new ImageEntity();
        try {
            File input = new File(pic);
            BufferedImage image = ImageIO.read(input);
            int width = image.getWidth();
            int height = image.getHeight();
            int k;
            imageEntity.setHeight(height);
            imageEntity.setWidth(width);
            imageEntity.setName(pic);

            for(int i=0; i<imageEntity.getHeight(); i++) {

//                System.out.println(i);
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

    @Override
    public ArrayList<char[]> gendata(String data){
        ArrayList<char[]> newData = new ArrayList<>();
        for (char i:
                data.toCharArray()) {
            if (Long.toBinaryString(i).length() == 7) newData.add(("0"+Long.toBinaryString(i)).toCharArray());
            else if (Long.toBinaryString(i).length() == 6) newData.add(("00"+Long.toBinaryString(i)).toCharArray());
            else newData.add(Long.toBinaryString(i).toCharArray());
        }
        return newData;
    }
}
