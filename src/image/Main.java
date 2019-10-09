package image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Main {
    public static void main(String[] args) throws Exception {
        int WIDTH = 800;
        int HEIGHT = 600;
//        System.out.println("축소하고자 하는 파일을 C:/test/c.bmp에 두세요");
//        System.out.println("몇배 축소?");
//        Scanner sc = new Scanner(System.in);
        int percent = 2;
        
        BMPHeader bmpHeader = new BMPHeader(WIDTH/percent, HEIGHT/percent);
        int[] header = bmpHeader.getHeader();
        int currentWidth = 0;
        int currentHeight = 0;
        File file = new File("C:/test/c.bmp");
        FileInputStream fileInputStream = new FileInputStream(file);
        File newFile = new File("C:/test/d.bmp");
        newFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        for (int i=0; i<54; i++) {
            fileInputStream.read(); //버림
            fileOutputStream.write(header[i]);
        }
        int cur = 0;
        while ((cur = fileInputStream.read()) != -1) {
            if ((currentWidth/3) % WIDTH == 0)
                currentHeight++;
            if (currentHeight % percent != 0 && (currentWidth/3) % percent != 0)
                fileOutputStream.write(cur);
            currentWidth++;
        }
        fileOutputStream.close();
        fileInputStream.close();
        System.out.println("C:/test/d.bmp로 저장완료");
    }
}
class BMPHeader {
    private int[] header = { 66, 77, 54, 249, 21, 0, 0, 0, 0, 0, 54, 0, 0, 0, 40, 0, 0, 0, 32, 3, 0, 0, 88, 2, 0, 0, 1,
            0, 24, 0, 0, 0, 0, 0, 0, 249, 21, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private int HEADER_SIZE = 54;
    private int FILE_SIZE_IDX = 2;
    private int WIDTH_IDX = 18;
    private int HEIGHT_IDX = 22;
    private int FILE_SIZE_WITHOUT_HEARER_SIZE_IDX = 34;
    public BMPHeader(int width, int height) {
        setToHeader(FILE_SIZE_IDX, 4, getFileSize(width, height));
        setToHeader(WIDTH_IDX, 4, width);
        setToHeader(HEIGHT_IDX, 4, height);
        setToHeader(FILE_SIZE_WITHOUT_HEARER_SIZE_IDX, 4, getFileSize(width, height)-HEADER_SIZE);
    }
    public int[] getHeader() {
        return header;
    }
    private int getFileSize(int width, int height) {
        return width * height * 3 + HEADER_SIZE;
    }
    
    private void setToHeader(int index, int size, int value) {
        int[] littleEndian = convertToLittleEndian(value, size);
        for (int i=0; i<4; i++)
            header[index+i] = littleEndian[i];
    }
    
    // 따로 뺄려다 맘
    private int[] convertToLittleEndian(int number, int size) {
        int[] littleEndian = new int[size];
        String hexString = Integer.toHexString(number);
        if (hexString.length()%2!=0)
            hexString = "0"+hexString;
        for (int i=0; i<hexString.length(); i+=2) {
            String hexChar = ""+hexString.charAt(i)+hexString.charAt(i+1);
            int hexNum = Integer.parseInt(hexChar, 16);
            littleEndian[((hexString.length()/2)-1)-i/2] = hexNum;
        }
        return littleEndian;
    }
    
    public void print() {
        for (int i=0; i<header.length; i++) {
            if (i%16 == 0)
                System.out.println();
            System.out.printf("%x\t", header[i]);
        }
    }
}