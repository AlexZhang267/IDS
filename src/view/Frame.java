package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.KeyEvent.VK_CONTROL;
import static java.awt.event.KeyEvent.VK_SHIFT;

/**
 * Created by alex on 7/24/16.
 */
public class Frame extends JFrame {

    JPanel contentPanel;

    FangLabel label0;
    FangLabel label1;

    //当前正在操作的label
    FangLabel currentLabel;

    List<FangLabel> labelList;


    Frame() {

        labelList = new ArrayList<>();


        label0 = new FangLabel(new ImageIcon("src/data/res.png"));
        System.out.println(label0.getWidth() + " " + label0.getHeight());
        label0.setBounds(-50, -50, 220, 165);
        labelList.add(label0);

        label1 = new FangLabel(new ImageIcon("src/data/res_rotate.png"));
        label1.setBounds(0, 0, 272, 272);

        labelList.add(label1);

        contentPanel = new JPanel();
//        contentPanel.setBackground(Color.yellow);
        contentPanel.setLayout(null);
        contentPanel.add(label1);
        contentPanel.add(label0);

        currentLabel = label0;


        add(contentPanel);
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                System.out.println("pressed");
                System.out.println(e.getKeyChar() + " " + (e.getKeyCode() == VK_SHIFT));
                System.out.println(e.getKeyCode() == VK_CONTROL);

                switch (e.getKeyCode()){
                    case VK_SHIFT:
                        for (FangLabel l:labelList){
                            l.setShiftPressed(true);
                        }
                        break;
                    case VK_CONTROL:
                        for (FangLabel l:labelList){
                            l.setControlPressed(true);
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                System.out.println("release");
                System.out.println(e.getKeyCode() == VK_SHIFT);
                System.out.println(e.getKeyCode() == VK_CONTROL);

                switch (e.getKeyCode()){
                    case VK_SHIFT:
                        for (FangLabel l : labelList){
                            l.setShiftPressed(false);
                            l.resizeProcessor.updataRate();
                        }
                    case VK_CONTROL:
                        for (FangLabel l: labelList){
                            l.setControlPressed(false);
                            l.rotateProcessor.updateCurrentRadian();
                        }
                        break;
                }

            }
        });

    }

    public static void main(String[] args) {
        Frame frame = new Frame();
    }


}


