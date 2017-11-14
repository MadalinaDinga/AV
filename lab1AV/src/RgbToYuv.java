public class RgbToYuv {
    public static YUVImage convert(RGBImage rgbImage) {
        YUVImage yuvImage = new YUVImage();
        yuvImage.setHeight(rgbImage.getHeight());
        yuvImage.setWidth(rgbImage.getWidth());
        yuvImage.setPixels(new YUV[yuvImage.getHeight()][yuvImage.getWidth()]);

        for (int i = 0; i < rgbImage.getHeight(); i++) {
            for (int j = 0; j < rgbImage.getWidth(); j++) {
                YUV yuv = new YUV();
                double y,u,v;
               /* y =0.299 * rgbImage.getPixels()[i][j].getRed() + 0.587 * rgbImage.getPixels()[i][j].getGreen() + 0.114
                        * rgbImage.getPixels()[i][j].getBlue();*/
                /*u = (-0.1687) * rgbImage.getPixels()[i][j].getRed() + (-0.3312) * rgbImage.getPixels()[i][j].getGreen()
                        + 0.5 * rgbImage.getPixels()[i][j].getBlue() + 128.0;*/
                /*v = 128.0 + 0.5 * rgbImage.getPixels()[i][j].getRed() + (-0.4186) * rgbImage.getPixels()[i][j].getGreen()
                        + (-0.0813) * rgbImage.getPixels()[i][j].getGreen();*/
                y = (0.257 * rgbImage.getPixels()[i][j].getRed()) + (0.504 * rgbImage.getPixels()[i][j].getGreen()) +
                        (0.098 * rgbImage.getPixels()[i][j].getBlue()) + 16;
                u = -(0.148 *  rgbImage.getPixels()[i][j].getRed()) - (0.291 *  rgbImage.getPixels()[i][j].getGreen()) +
                        (0.439 *  rgbImage.getPixels()[i][j].getBlue()) + 128;
                v = (0.439 * rgbImage.getPixels()[i][j].getRed()) - (0.368 * rgbImage.getPixels()[i][j].getGreen()) -
                        (0.071 * rgbImage.getPixels()[i][j].getBlue()) + 128;
                yuv.setY(y);
                yuv.setU(u);
                yuv.setV(v);

                yuvImage.getPixels()[i][j] = yuv;
            }
        }
        return yuvImage;
    }

    public static RGBImage convertToRGB(YUVImage yuvImage) {
        RGBImage rgbImage = new RGBImage();
        rgbImage.setHeight(yuvImage.getHeight());
        rgbImage.setWidth(yuvImage.getWidth());
        rgbImage.setPixels(new RGB[rgbImage.getHeight()][rgbImage.getWidth()]);

        for (int i = 0; i < rgbImage.getHeight(); i++) {
            for (int j = 0; j < rgbImage.getWidth(); j++) {
                RGB rgb = new RGB();
                double red,green,blue;
                red =1.164 * (yuvImage.getPixels()[i][j].getY() - 16.0) + 1.596 * (yuvImage.getPixels()[i][j].getV() - 128.0);
                green =1.164 * (yuvImage.getPixels()[i][j].getY() - 16) - 0.813 *
                        (yuvImage.getPixels()[i][j].getV() - 128.0) - 0.391 * (yuvImage.getPixels()[i][j].getU() - 128);
                blue = 1.164 * (yuvImage.getPixels()[i][j].getY() - 16.0) + 2.018 * (yuvImage.getPixels()[i][j].getU() - 128.0);

                rgb.setRed(red);
                rgb.setGreen(green);
                rgb.setBlue(blue);
                rgbImage.getPixels()[i][j] = rgb;
            }
        }

        return rgbImage;
    }
}
