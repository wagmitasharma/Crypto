package com.Crypto;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyCode {

    public static ArrayList<char[]> gendata(String data){
        ArrayList<char[]> newData = new ArrayList<>();
        for (char i:
             data.toCharArray()) {
            if (Long.toBinaryString(i).length() == 7) newData.add(("0"+Long.toBinaryString(i)).toCharArray());
            else if (Long.toBinaryString(i).length() == 6) newData.add(("00"+Long.toBinaryString(i)).toCharArray());
            else newData.add(Long.toBinaryString(i).toCharArray());
                System.out.println(Long.toBinaryString(i).toCharArray());
        }
        return newData;
    }

    public static ArrayList<ArrayList<Integer>> modPix(ImageEntity imageEntity, String data){
        ArrayList<char[]> dataList = gendata(data);
//        int lenData = dataList.size();
        ArrayList<ArrayList<Integer>> newPix = new ArrayList<>();
        List<ArrayList<Integer>> newSubPix;
        int k=0;
        for (int i=0; i<dataList.size(); i++) {
            newSubPix = imageEntity.getPixels().subList(k,k+3);
            System.out.println(newSubPix);
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
        System.out.println("in modpix");
        newPix.addAll(imageEntity.getPixels().subList(newPix.size(),imageEntity.getPixels().size()));
        imageEntity.setPixels(newPix);
        System.out.println(makeEncodedImage(imageEntity, data.length()*3));
        return newPix;
    }
    public static ImageEntity getPixel(String pic) {
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

            for(int i=0; i<height; i++) {

                for(int j=0; j<width; j++) {
                    ArrayList<Integer> p = new ArrayList<>();
                    k = image.getRGB(i, j);
                    p.add(0, (k >> 24) & 0xff);
                    p.add(1, (k >> 16) & 0xff);
                    p.add(2, (k >> 8) & 0xff);
                    p.add(3, k & 0xff);
                    pixel.add(p);
                }
            }
        } catch (Exception e) {}
        System.out.println("in Pixel");
        imageEntity.setPixels(pixel);
        return imageEntity;
    }
    public static String makeEncodedImage(ImageEntity imageEntity, int msgLenth){
        int p;
        int k =0;
        int flag = 0;
        try {
            File output = new File(imageEntity.getName().substring(0,imageEntity.getName().length()-4) + "Encoded.png");
            BufferedImage img = ImageIO.read(new File (imageEntity.getName()));
            BufferedImage newImg = new BufferedImage(img.getWidth(),img.getHeight(), img.getType());
            newImg.getGraphics().drawImage(img,0,0,null);
            for (int i = 0; i < newImg.getHeight(); i++) {
                for (int j = 0; j < newImg.getWidth(); j++) {
                    p = (imageEntity.getPixels().get(k).get(0) << 24) | (imageEntity.getPixels().get(k).get(1) << 16)
                            | (imageEntity.getPixels().get(k).get(2) << 8) | imageEntity.getPixels().get(k).get(3);
                    k += 1;
                    newImg.setRGB(i, j, p);
                    msgLenth--;
                    if (msgLenth == 0) { flag=1; break;}
                }
                if (flag==1) break;
            }
            ImageIO.write(newImg,"png",output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageEntity.getName();
    }
    public static String decodeMsg(String pic){
        ImageEntity imageEntity = getPixel(pic);
        System.out.println("im decode msg"+imageEntity.getPixels());
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
    public static void mainRun(){
        String pic = "D:\\LiloStichSample.jpg";
        ImageEntity imageEntity = getPixel(pic);
        System.out.println(imageEntity.getPixels());
        imageEntity.setPixels(modPix(imageEntity, "Aaradhana Sharma !!!!"));
        System.out.println(imageEntity.getPixels());
        System.out.println(decodeMsg("D:\\LiloStichSampleEncoded.png"));
    }
}
