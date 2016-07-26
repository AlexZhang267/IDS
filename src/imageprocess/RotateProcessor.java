package imageprocess;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

/**
 * Created by alex on 7/24/16.
 */
public class RotateProcessor {
    public static final int LEFTTOP = 0;
    public static final int CENTER = 1;

    private BufferedImage sourceImage;
    double currentRadian = 0;
    double tmpRadian;

    public BufferedImage rotate(int center, double radian) {


        radian = currentRadian + radian;
        tmpRadian = radian;
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();

        int[] res = calculateWidthHeight(width, height, radian);
        int[] bias = calculateBias(res[0], width, height, radian);

        width = res[0];
        height = res[1];

        System.out.println("width height: " + width + " " + height + " radian:" + radian + " sin" + Math.sin(radian) + "cos:" + Math.cos(radian));
        int x = 0;
        int y = 0;
        if (center == CENTER) {
            x = width / 2;
            y = height / 2;
        }

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = img.createGraphics();
        graphics2D.translate(bias[0], bias[1]);

        graphics2D.rotate(radian, x, y);
        graphics2D.drawImage(sourceImage, 0, 0, null);
        graphics2D.dispose();

        return img;
    }

    public void setSourceImage(BufferedImage sourceImage) {
        this.sourceImage = sourceImage;
    }

    public void updateCurrentRadian() {
        currentRadian = tmpRadian;
    }

    private int[] calculateWidthHeight(int width, int height, double angel) {
        int nw = (int) (width * Math.abs(Math.cos(angel)) + height * Math.abs(Math.sin(angel)));
        int nh = (int) (width * Math.abs(Math.sin(angel)) + height * Math.abs(Math.cos(angel)));
        return new int[]{nw, nh};
    }

    private int[] calculateBias(int newWidth, int oldWidth, int oldHeight, double angel) {
//        return (int) (newWidth / 2 + (oldHeight / 2) * Math.sin(angel) - (oldWidth / 2) * Math.cos(angel));
        if (angel > 2*Math.PI)
            angel = angel % (2*Math.PI);

        if (angel < Math.PI / 2) {
            return new int[]{(int) (oldHeight * Math.sin(angel)), 0};
        } else if (angel < Math.PI) {
            return new int[]{(int) (oldHeight * Math.cos(angel - Math.PI / 2) + oldWidth * Math.sin(angel - Math.PI / 2)), (int) (oldHeight * Math.sin(angel - Math.PI / 2))};
        } else if ((angel < 3 * Math.PI / 2)) {
            return new int[]{(int) (oldWidth * Math.cos(angel - Math.PI)), (int) (oldWidth * Math.sin(angel - Math.PI) + oldHeight * Math.cos(angel - Math.PI))};
        } else {
            return new int[]{0, (int) (oldWidth * Math.cos(angel - 3 * Math.PI / 2))};
        }
    }
}
