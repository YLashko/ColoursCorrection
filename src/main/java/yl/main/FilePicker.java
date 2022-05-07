package yl.main;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.event.*;

public class FilePicker {

    private String imagesFolder = Options.imagesFolder;
    private String imageType = ".png";
    ArrayList<JButton> buttons = new ArrayList<>();
    JFrame frame = new JFrame();

    public FilePicker(UI ui) throws IOException{
        ArrayList<String> images = getFilesInCurrentDir();
        images = filterType(images, imageType);

        JButton b;
        int counter = 0;

        for (String image: images){
            b = new JButton(image);
            b.setBounds(0, counter * 40, 286, 35);
            b.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent e) { 
                    ui.setFileName(image);
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                } 
            });
            frame.add(b);
            buttons.add(new JButton());
            counter++;
        }

        frame.setSize(300, (images.size() + 1) * 40 - 8);
        frame.setLayout(null);
        frame.setVisible(true);

    }

    public ArrayList<String> getFilesInCurrentDir() {
        File f = new File(imagesFolder);
        System.out.println(f);
        ArrayList<String> filesList = new ArrayList<String>(Arrays.asList(f.list()));
        return filesList;
    }

    public ArrayList<String> filterType(ArrayList<String> arr, String type) {
        ArrayList<String> filteredArr = new ArrayList<String>();
        for (String el: arr) {
            if (el.endsWith(type)) { filteredArr.add(el); }
        }
        return filteredArr;
    }
    public static void main(String[] args) {

    }
}