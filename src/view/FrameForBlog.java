package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 7/25/16.
 */
public class FrameForBlog extends JFrame {

    JPanel contentPanel;
    LabelForBlog labelForBlog;
    Button okButton;

    FrameForBlog() {

        contentPanel = new JPanel();
        contentPanel.setLayout(new FlowLayout());


        ImageIcon icon = new ImageIcon("src/data/cartoon.jpg");
        icon.setImage(icon.getImage().getScaledInstance(1000, icon.getIconHeight() * 1000 / icon.getIconWidth(), Image.SCALE_AREA_AVERAGING));
        labelForBlog = new LabelForBlog(icon);
        labelForBlog.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());
        contentPanel.add(labelForBlog);

        okButton = new Button("OK");
        contentPanel.add(okButton);
        okButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                labelForBlog.process();
            }
        });


        add(contentPanel);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new FrameForBlog();
    }
}

class LabelForBlog extends JLabel {
    //to record the coordinates
    List<int[]> coordinates;

    LabelForBlog() {
        super();
        coordinates = new ArrayList<>();
    }

    LabelForBlog(ImageIcon imageIcon) {
        super(imageIcon);
        coordinates = new ArrayList<>();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                System.out.println(e.getX() + " " + e.getY());
                int[] coor = new int[]{e.getX(), e.getY()};
                boolean contain = false;
                for (int[] c : coordinates) {
                    if (c[0] == coor[0] && c[1] == coor[1]) {
                        contain = true;
                        break;
                    }
                }
                if (!contain) {
                    coordinates.add(coor);
                    System.out.println("not contain");
                    repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        System.out.println("paint comComponent");
        if (coordinates.size() > 0) {
            int x0 = coordinates.get(0)[0];
            int y0 = coordinates.get(0)[1];
            int x1 = 0;
            int y1 = 0;

            //如果不加这个-3 和 +5 * 和线就没有完全重合,应该是drawString的原因
            g.drawString("*", x0 - 3, y0 + 5);
            for (int i = 1; i < coordinates.size(); i++) {
                x1 = coordinates.get(i)[0];
                y1 = coordinates.get(i)[1];
                g.drawString("*", x1 - 3, y1 + 5);
                g.drawLine(x0, y0, x1, y1);
                x0 = x1;
                y0 = y1;
            }
        }
    }


    private int[][] getMask() {
        int x = this.getX();
        int y = this.getY();

        int[][] points = new int[coordinates.size()][2];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i][0] = coordinates.get(i)[0];
            points[i][1] = coordinates.get(i)[1];
        }

        GeneralPath path = new GeneralPath();
        path.moveTo(points[0][0], points[0][1]);

        for (int i = 1; i < points.length; i++) {
            path.lineTo(points[i][0], points[i][1]);
        }

        int[][] mask = new int[this.getIcon().getIconHeight()][this.getIcon().getIconWidth()];
        for (int i = 0; i < this.getIcon().getIconHeight(); i++) {
            for (int j = 0; j < this.getIcon().getIconWidth(); j++) {
                //值得注意的是contains(j,i)
                mask[i][j] = path.contains(j, i) ? 1 : 0;
            }
        }
        return mask;
    }

    public void process(){
        int[][] mask = getMask();
        BufferedImage img = new BufferedImage(this.getIcon().getIconWidth(), this.getIcon().getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = img.createGraphics();
        g.drawImage(((ImageIcon) this.getIcon()).getImage(),0,0,null);
        g.dispose();
        BufferedImage resImage = alphaProcess(mask,img);
        this.setIcon(new ImageIcon(resImage));

    }

    private BufferedImage alphaProcess(int[][] mask, BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        System.out.println(width + " " + height);

        BufferedImage resImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

       for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = bufferedImage.getRGB(i, j);
                int r = (0xff & rgb);
                int g = (0xff & (rgb >> 8));
                int b = (0xff & (rgb >> 16));
                if (mask[j][i] == 1)
                    rgb = r + (g << 8) + (b << 16) + (100 << 24);
                else{
                    rgb = r + (g<<8) + (b<<16)+(255<<24);
                }
                resImage.setRGB(i, j, rgb);
            }
        }
        return resImage;


    }
}
