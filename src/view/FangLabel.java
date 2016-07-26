package view;

import imageprocess.ResizeProcessor;
import imageprocess.RotateProcessor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import static view.FangLabel.PAINTCODE.DRAWSTRING;
import static view.FangLabel.PAINTCODE.REMOVESTRING;

/**
 * Created by alex on 7/26/16.
 */
public class FangLabel extends JLabel {

    int x0;
    int y0;

    //九个点围成一个方框
    int[][][] coor;


    private boolean shiftPressed;
    private boolean controlPressed;

    enum PAINTCODE {DEFAULT, DRAWSTRING, REMOVESTRING}

    PAINTCODE currentCode = PAINTCODE.DEFAULT;


    ResizeProcessor resizeProcessor;
    RotateProcessor rotateProcessor;

    FangLabel() {

    }

    FangLabel(ImageIcon imageIcon) {
        super(imageIcon);

        this.setBackground(Color.GREEN);


        BufferedImage img = getBufferedImage();
        resizeProcessor = new ResizeProcessor();
        resizeProcessor.setSourceImage(img);

        rotateProcessor = new RotateProcessor();
        rotateProcessor.setSourceImage(img);

        coor = new int[3][3][2];
        initCoor();

        /**
         *   获取按下时的坐标
         */
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                x0 = e.getX();
                y0 = e.getY();
                System.out.println(x0 + " " + y0);
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (!shiftPressed && !controlPressed) {
                    Point p = getLocation();
                    setLocation(p.x + e.getX() - x0, p.y + e.getY() - y0);
                }

                if (shiftPressed) {

                    int[] location0 = new int[]{x0, y0};
                    int[] location1 = new int[]{e.getX(), e.getY()};
                    System.out.println(location0[0] + " " + location0[1]);
                    System.out.println(location1[0] + " " + location1[1]);
                    double rate = calculateResizeRate(location0, location1);
                    System.out.println("rate: " + rate);
                    BufferedImage resImg = resizeProcessor.resize(rate);
                    setIcon(new ImageIcon(resImg));
                    setBounds(getLocation().x, getLocation().y, getIcon().getIconWidth(), getIcon().getIconHeight());
                    currentCode = DRAWSTRING;
                    initCoor();
                    repaint();

                }
                if (controlPressed) {
                    int[] location0 = new int[]{x0, y0};
                    int[] location1 = new int[]{e.getX(), e.getY()};
                    double radian = calculateRotateRadian(location0,location1);
                    System.out.println("Radian: "+radian);


                    int[] centerPrev = coor[1][1];
                    System.out.println(centerPrev[0] + " "+centerPrev[1]);


                    BufferedImage img = rotateProcessor.rotate(RotateProcessor.LEFTTOP,radian);
                    setIcon(new ImageIcon(img));
                    setBorder(BorderFactory.createLineBorder(Color.red));
                    initCoor();
                    int[] centerNew = coor[1][1];
                    System.out.println(centerNew[0] + " "+centerNew[1]);

                    setBounds(getLocation().x, getLocation().y , getIcon().getIconWidth(), getIcon().getIconHeight());

                }
            }
        });


    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (currentCode) {
            case DRAWSTRING:
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        g.drawString("*", coor[i][j][0], coor[i][j][1] + 5);
                    }
                }
                break;
            case REMOVESTRING:
                break;
        }

    }


    //计算缩放比
    private double calculateResizeRate(int[] location0, int[] location1) {
        int[] leftTop = coor[0][0];
        int[] l0 = new int[]{Math.abs(location0[0] - leftTop[0]), Math.abs(location0[1] - leftTop[1])};
        int[] l1 = new int[]{Math.abs(location1[0] - leftTop[0]), Math.abs(location1[1] - leftTop[1])};

        System.out.println("l0: " + l0[0] + " " + l0[1]);
        System.out.println("l1: " + l1[0] + " " + l1[1]);
        return Math.abs(((double) (l1[0] + l1[1])) / (double) (l0[0] + l0[1]));
    }

    //计算旋转的角度
    private double calculateRotateRadian(int[] location0, int[] location1){
//        int[] center = coor[1][1];
//
//        int[] l0 = new int[]{Math.abs(location0[0] - center[0]), Math.abs(location0[1] - center[1])};
//        int[] l1 = new int[]{Math.abs(location1[0] - center[0]), Math.abs(location1[1] - center[1])};
        int[] leftTop = coor[0][0];
        int[] l0 = new int[]{Math.abs(location0[0] - leftTop[0]), Math.abs(location0[1] - leftTop[1])};
        int[] l1 = new int[]{Math.abs(location1[0] - leftTop[0]), Math.abs(location1[1] - leftTop[1])};

        System.out.println("l0: " + l0[0] + " " + l0[1]);
        System.out.println("l1: " + l1[0] + " " + l1[1]);

        return Math.acos((l1[0]*l0[0] + l1[1]*l0[1])/(Math.sqrt(l0[0]*l0[0] + l0[1]*l0[1])*Math.sqrt(l1[0]*l1[0] + l1[1]*l1[1])));
    }


    private void initCoor() {
        int width = this.getIcon().getIconWidth();
        int height = this.getIcon().getIconHeight();
        BufferedImage img = getBufferedImage();


        int left = width, right = 0, top = height, bottom = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = img.getRGB(i, j);
                if (((rgb >> 24) & 0xff) > 0) {
                    if (i < left)
                        left = i;
                    if (i > right)
                        right = i;
                    if (j < top)
                        top = j;
                    if (j > bottom)
                        bottom = j;
                }
            }
        }
        left += 2;
        right -= 2;
        top += 2;
        bottom -= 2;

        //初始化第一行
        coor[0][0][0] = left;
        coor[0][0][1] = top;
        coor[0][1][0] = (left + right) / 2;
        coor[0][1][1] = top;
        coor[0][2][0] = right;
        coor[0][2][1] = top;

        // 初始化第二行
        coor[1][0][0] = left;
        coor[1][0][1] = (top + bottom) / 2;
        coor[1][1][0] = (left + right) / 2;
        coor[1][1][1] = (top + bottom) / 2;
        coor[1][2][0] = right;
        coor[1][2][1] = (top + bottom) / 2;

        //初始化第三行
        coor[2][0][0] = left;
        coor[2][0][1] = bottom;
        coor[2][1][0] = (left + right) / 2;
        coor[2][1][1] = bottom;
        coor[2][2][0] = right;
        coor[2][2][1] = bottom;

    }

    private void updateCoor(double rate){

        int width = (int) ((coor[0][2][0] - coor[0][0][0])*rate);
        int height = (int)((coor[2][0][1] - coor[0][0][1])*rate);
//        coor[0][0][0] = left;
//        coor[0][0][1] = top;
        coor[0][1][0] = coor[0][0][0]+width/2;
//        coor[0][1][1] = top;
        coor[0][2][0] = coor[0][0][0]+width;

        // 初始化第二行
//        coor[1][0][0] = left;
        coor[1][0][1] = coor[0][0][0]+height/2;
        coor[1][1][0] = coor[0][0][0]+width/2;
        coor[1][1][1] = coor[0][0][0]+height/2;
        coor[1][2][0] = coor[0][0][0]+width;
        coor[1][2][1] = coor[0][0][0]+height/2;

        //初始化第三行
//        coor[2][0][0] = left;
        coor[2][0][1] = coor[0][0][0]+height;
        coor[2][1][0] = coor[0][0][0]+width/2;
        coor[2][1][1] = coor[0][0][0]+height;
        coor[2][2][0] =  coor[0][0][0]+width;
        coor[2][2][1] = coor[0][0][0]+height;
    }

    private BufferedImage getBufferedImage() {
        BufferedImage img = new BufferedImage(this.getIcon().getIconWidth(), this.getIcon().getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = img.createGraphics();
        g.drawImage(((ImageIcon) this.getIcon()).getImage(), 0, 0, null);
        g.dispose();
        return img;
    }


    public boolean isControlPressed() {
        return controlPressed;
    }

    public boolean isShiftPressed() {
        return shiftPressed;
    }

    public void setControlPressed(boolean controlPressed) {
        this.controlPressed = controlPressed;
        if (controlPressed) {
            currentCode = DRAWSTRING;
            repaint();
        } else {
            currentCode = REMOVESTRING;
            repaint();
        }
    }

    public void setShiftPressed(boolean shiftPressed) {
        this.shiftPressed = shiftPressed;
        if (shiftPressed) {
            currentCode = DRAWSTRING;
            repaint();
        } else {
            currentCode = REMOVESTRING;
            repaint();
        }
    }
}