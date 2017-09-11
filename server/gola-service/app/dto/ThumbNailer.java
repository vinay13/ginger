package dto;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by senthil
 */
public class ThumbNailer {

    /*public static void main(String[] args) throws Exception{
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        new ThumbNailer().transform(1, byteArrayOutputStream, new ByteArrayInputStream(FileUtils.readAllBytes("F://temp//upload//love/0.gif")));
        FileUtils.writeAllBytes(byteArrayOutputStream.toByteArray(), "test3.gif");
    }*/

    public Dimension transform(int quality, ByteArrayOutputStream output,
                          ByteArrayInputStream input) throws Exception {
        /*PatchedGIFImageReader reader = new PatchedGIFImageReader(null);
        ImageInputStream iStream = ImageIO.createImageInputStream(input);



        Image image = reader.read(0);*/
        BufferedImage image = javax.imageio.ImageIO.read(input);
        int thumbWidth = 248;
        int thumbHeight = 100;
        int imageWidth = 0;
        int imageHeight = 0;
        if(image == null) {
            imageWidth = thumbWidth;
            imageHeight = thumbHeight;
        }else{

//        val stream = ImageIO.read(new ByteArrayInputStream(bytes))

            //Image image = javax.imageio.ImageIO.read(input);

            double thumbRatio = (double) thumbWidth / (double) thumbHeight;
            imageWidth = image.getWidth(null);
            imageHeight = image.getHeight(null);
            double imageRatio = (double) imageWidth / (double) imageHeight;
            if (thumbRatio < imageRatio) {
                thumbHeight = (int) (thumbWidth / imageRatio);
            } else {
                thumbWidth = (int) (thumbHeight * imageRatio);
            }

            if (imageWidth < thumbWidth && imageHeight < thumbHeight) {
                thumbWidth = imageWidth;
                thumbHeight = imageHeight;
            } else if (imageWidth < thumbWidth)
                thumbWidth = imageWidth;
            else if (imageHeight < thumbHeight)
                thumbHeight = imageHeight;
        }

        BufferedImage thumbImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setBackground(Color.WHITE);
        graphics2D.setPaint(Color.WHITE);
        graphics2D.fillRect(0, 0, thumbWidth, thumbHeight);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, imageWidth, imageHeight, null);

        javax.imageio.ImageIO.write(thumbImage, "JPG", output);
        if(image == null) {
            return new Dimension(imageWidth, imageHeight);
        }
        return new Dimension(image.getWidth(), image.getHeight());
    }
}
