package yl.main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.*;
import java.awt.Image;
import java.lang.Math;

public class UI {

    String fileName = null;

    JFrame frame = new JFrame();
    JButton filePickButton = new JButton("Pick image");
    JButton compileButton = new JButton("Compile");

    JCheckBox preCalculateCB = new JCheckBox("Pre-calculate colors");

    JTextArea fileNameField = new JTextArea();
    JTextArea redFuncField = new JTextArea("r");
    JTextArea greenFuncField = new JTextArea("g");
    JTextArea blueFuncField = new JTextArea("b");
    JTextArea redInfoField = new JTextArea("red = ");
    JTextArea greenInfoField = new JTextArea("green = ");
    JTextArea blueInfoField = new JTextArea("blue = ");

    JLabel imageLeftHolder = new JLabel();
    ImageIcon imageLeft = new ImageIcon();

    JLabel imageRightHolder = new JLabel();
    ImageIcon imageRight = new ImageIcon();

    BufferedImage editingImageBuffered;
    Image editingImage;
    UI ui = this;

    SmallMathCompiler smc = new SmallMathCompiler();
    TernaryFunction ternaryRed;
    TernaryFunction ternaryGreen;
    TernaryFunction ternaryBlue;
    int[] preCalculatedRed = new int[256];
    int[] preCalculatedGreen = new int[256];
    int[] preCalculatedBlue = new int[256];

    public UI() {
        addFieldsAndButtons();
        compile();
    }

    private void addFieldsAndButtons(){

        filePickButton.setBounds(10, 10, 100, 50);
        filePickButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                try {
                    new FilePicker(ui);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } 
        });

        compileButton.setBounds(1160, 10, 100, 100);
        compileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                compile();
            }
        });

        preCalculateCB.setBounds(1110, 120, 150, 30);

        redFuncField.setBounds(170, 10, 600, 27);
        greenFuncField.setBounds(170, 47, 600, 27);
        blueFuncField.setBounds(170, 84, 600, 27);

        redInfoField.setBounds(120, 10, 40, 27);
        greenInfoField.setBounds(120, 47, 40, 27);
        blueInfoField.setBounds(120, 84, 40, 27);
        redInfoField.setEditable(false);
        greenInfoField.setEditable(false);
        blueInfoField.setEditable(false);

        fileNameField.setBounds(10, 60, 100, 50);
        fileNameField.setEditable(false);

        imageLeftHolder.setBounds(10, 160, 500, 300);
        imageRightHolder.setBounds(770, 160, 500, 300);

        frame.add(compileButton);
        frame.add(filePickButton);

        frame.add(preCalculateCB);

        frame.add(fileNameField);
        frame.add(redFuncField);
        frame.add(greenFuncField);
        frame.add(blueFuncField);
        frame.add(redInfoField);
        frame.add(greenInfoField);
        frame.add(blueInfoField);
        frame.add(imageLeftHolder);
        frame.add(imageRightHolder);

        frame.setSize(1280, 720);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public void compile(){
        ternaryRed = new TernaryFunction(redFuncField.getText()); ternaryRed.compile();
        ternaryGreen = new TernaryFunction(greenFuncField.getText()); ternaryGreen.compile();
        ternaryBlue = new TernaryFunction(blueFuncField.getText()); ternaryBlue.compile();
        if (preCalculateCB.isSelected()){
            for (int i = 0; i < 256; i++){
                smc.setVariable("r", (double) i);
                smc.setVariable("g", (double) i);
                smc.setVariable("b", (double) i);
                preCalculatedRed[i] = cut_255((int) smc.processTernary(ternaryRed));
                preCalculatedGreen[i] = cut_255((int) smc.processTernary(ternaryGreen));
                preCalculatedBlue[i] = cut_255((int) smc.processTernary(ternaryBlue));
            }
        }
        if (fileName != null){
            try { setRightImage(processImage(ImageIO.read(new File(Options.imagesFolder + fileName)))); } catch (IOException e) {}
        }
        
    }

    public String addNewlines(String text, int length){
        for (int i = length; i < text.length(); i+=length){
            text = text.substring(0, i) + "\n" + text.substring(i);
        }
        return text;
    }

    public void setLeftImage(String filename, JLabel imageHolder){
        ImageIcon image = new ImageIcon(); 
        System.out.println("Setting " + filename);
        try {
            editingImageBuffered = ImageIO.read(new File(filename));
            editingImage = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image resizedImage = editingImageBuffered.getScaledInstance((int)( (float) editingImageBuffered.getWidth() /
                                                                 (float) editingImageBuffered.getHeight() * 300), 300, Image.SCALE_SMOOTH);
        image.setImage(resizedImage);
        imageHolder.setIcon(image);
    } 

    public void setRightImage(BufferedImage image){
        ImageIcon imagei = new ImageIcon(); 
        Image resizedImage = image.getScaledInstance((int)( (float) image.getWidth() /
                                                    (float) image.getHeight() * 300), 300, Image.SCALE_SMOOTH);
        imagei.setImage(resizedImage);
        imageRightHolder.setIcon(imagei);
    }

    public BufferedImage processImage(BufferedImage image) {
        BufferedImage imageIn = image;
        BufferedImage imageOut = 
        new BufferedImage(imageIn.getWidth(), imageIn.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        int width = imageIn.getWidth();
        int height = imageIn.getHeight();
        int[] imageInPixels = imageIn.getRGB(0, 0, width, height, null, 0, width);
        int[] imageOutPixels = new int[imageInPixels.length];

        for (int i = 0; i < imageInPixels.length; i++) {
            int alpha = (imageInPixels[i] & 0xFF000000) >> 24;
            int red = (imageInPixels[i] & 0x00FF0000) >> 16;
            int green = (imageInPixels[i] & 0x0000FF00) >> 8;
            int blue = (imageInPixels[i] & 0x000000FF);
            
            int[] results = processRGB(red, green, blue, i);

            imageOutPixels[i] = (alpha & 0xFF) << 24
                            | (results[0] & 0xFF) << 16
                            | (results[1] & 0xFF) << 8
                            | (results[2] & 0xFF);
    
        }
        imageOut.setRGB(0, 0, width, height, imageOutPixels, 0, width);
        return imageOut;
    }

    private int[] processRGB(int r, int g, int b, int num){
        int resR = 0; int resG = 0; int resB = 0;
        if (preCalculateCB.isSelected()){
            resR = preCalculatedRed[r];
            resG = preCalculatedGreen[g];
            resB = preCalculatedBlue[b];
        } else {
            smc.setVariable("r", (double) r);
            smc.setVariable("g", (double) g);
            smc.setVariable("b", (double) b);
            smc.setVariable("num", (double) num);
            resR = cut_255((int) smc.processTernary(ternaryRed));
            resG = cut_255((int) smc.processTernary(ternaryGreen));
            resB = cut_255((int) smc.processTernary(ternaryBlue));
        }
        return new int[] {resR, resG, resB};
    }

    public void setFileName(String FileName){
        this.fileName = FileName;
        fileNameField.setText(addNewlines(FileName, 14));
        setLeftImage(Options.imagesFolder + FileName, imageLeftHolder);
        if (redFuncField.getText() != "" && greenFuncField.getText() != "" && blueFuncField.getText() != ""){
            try { setRightImage(processImage(ImageIO.read(new File(Options.imagesFolder + FileName)))); } catch (IOException e) {}
        }
    }

    private int cut_255(int num){
        return Math.max(0, Math.min(255, num));
    }

    public static void main(String[] args) {
        UI app = new UI();
    }
}