package imageprocess;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by alex on 7/24/16.
 */
public class ResizeProcessor {
    private BufferedImage sourceImage;
    private double currentRate;
    private double tmpRate;

    public BufferedImage resize(double rate){

        if (sourceImage == null){
            System.out.println("sourceImage is null");
            return null;
        }

        double actualRate = currentRate * rate;
        int width = (int) (sourceImage.getWidth()*actualRate);
        int height = (int)(sourceImage.getHeight()*actualRate);


        if (width <=0 || height <=0){
            if (sourceImage.getHeight() > sourceImage.getWidth()){
                width = 10;
                height = sourceImage.getHeight()/sourceImage.getWidth();
            }else {
                height = 10;
                width = sourceImage.getWidth()/sourceImage.getHeight();
            }
        }

        BufferedImage resImage = new BufferedImage(width,height,BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics2D = resImage.createGraphics();
        graphics2D.drawImage(sourceImage.getScaledInstance(width,height,Image.SCALE_AREA_AVERAGING),0,0,null);
        graphics2D.dispose();
        tmpRate = actualRate;
        return resImage;
    }

    /**
     *  重新设置源图像并将当前缩放比置为1
     * @param image
     */
    public void setSourceImage(BufferedImage image){
        sourceImage = image;
        currentRate = 1;
    }

    public void updataRate(){
        currentRate = tmpRate;
    }
}
