package imageprocess;

import java.awt.image.BufferedImage;

/**
 * Created by alex on 7/24/16.
 */
public class AlphaProcessor {
    /**
     * 处理透明度
     */
    public BufferedImage alphaProcess(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        System.out.println(width+" "+height);

        BufferedImage resImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

        double grayMean = 0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = bufferedImage.getRGB(i,j);
                int r = (0xff&rgb);
                int g = (0xff&(rgb>>8));
                int b = (0xff&(rgb>>16));
                grayMean += (r*0.299+g*0.587+b*0.114);
            }
        }
        grayMean = grayMean/(width*height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = bufferedImage.getRGB(i,j);
                int r = (0xff&rgb);
                int g = (0xff&(rgb>>8));
                int b = (0xff&(rgb>>16));
                double gray = (r*0.299+g*0.587+b*0.114);
                if (gray>grayMean){
                    rgb = r + (g << 8) + (b << 16);
                }
                resImage.setRGB(i,j,rgb);
            }
        }
        return resImage;


    }

}
