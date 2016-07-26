package imageprocess;

/**
 * Created by alex on 7/23/16.
 */
public class Test {
    public static void main(String[] args) {
//        System.out.println(Integer.toBinaryString(0x000f));

        System.out.println(Math.toDegrees(45 * Math.PI /180));
        Processor p = new Processor("src/data/test2.jpg");
//        p.alphaProcess();
        p.rotateProcessor.setSourceImage(p.bufferedImage);
        p.rotate(RotateProcessor.LEFTTOP,45*Math.PI/180);
        p.setResizeProcessorSourceImage(p.bufferedImage);
        p.resize(1.5) ;
    }
}
