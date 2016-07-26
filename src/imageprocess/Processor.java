package imageprocess;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by alex on 7/23/16.
 */
public class Processor {
    BufferedImage bufferedImage;

    AlphaProcessor alphaProcessor;
    RotateProcessor rotateProcessor;
    ResizeProcessor resizeProcessor;

    Processor(String imagePath) {
        bufferedImage = initBufferedImage(imagePath);
        alphaProcessor = new AlphaProcessor();
        rotateProcessor = new RotateProcessor();
        resizeProcessor = new ResizeProcessor();
    }

    private BufferedImage initBufferedImage(String imagePath) {
        File file = new File(imagePath);
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void alphaProcess() {
        BufferedImage img =alphaProcessor.alphaProcess(bufferedImage);
        writeBufferedImage(img,"src/data/res.png");
    }


    /**
     * 旋转图片
     */

    public void rotate(int center, double angel) {
        BufferedImage img = rotateProcessor.rotate(center, angel);
        writeBufferedImage(img,"src/data/res_rotate.png");

    }


    public void setResizeProcessorSourceImage(BufferedImage image){
        resizeProcessor.setSourceImage(image);
    }
    public void resize(double rate){
        BufferedImage image = resizeProcessor.resize(rate);
        writeBufferedImage(image,"src/data/res_resize.png");
    }

    /**
     * 写BufferedImage到指定路径下
     * @param img 源图像
     * @param filePath 路径
     */
    private void writeBufferedImage(BufferedImage img,String filePath){
        String format = filePath.substring(filePath.indexOf('.')+1);
        System.out.println(format);
        try {
            ImageIO.write(img,format,new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
