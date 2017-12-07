
public class Decoder {
    public static YUVImage decodeYUVImage(Block[] yBlocks, Block[] uBlocks, Block[] vBlocks) {
        YUVImage yuvImage = new YUVImage();
        yuvImage.setHeight(yBlocks[yBlocks.length - 1].getPosX() + 8);
        yuvImage.setWidth(yBlocks[yBlocks.length - 1].getPosY() + 8);
        yuvImage.setPixels(new YUVPixel[yuvImage.getHeight()][yuvImage.getWidth()]);

        for (int i = 0; i < yBlocks.length; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    YUVPixel yuvPixel = new YUVPixel();
                    yuvPixel.setY(yBlocks[i].getValues()[j][k]);
                    yuvImage.getPixels()[yBlocks[i].getPosX() + j][yBlocks[i].getPosY() + k] = yuvPixel;
                }
            }
        }

        for (int i = 0; i < uBlocks.length; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    yuvImage.getPixels()[uBlocks[i].getPosX() + 2 * j][uBlocks[i].getPosY() + 2 * k].setU(uBlocks[i].getValues()[j][k]);
                    yuvImage.getPixels()[uBlocks[i].getPosX() + 2 * j + 1][uBlocks[i].getPosY() + 2 * k].setU(uBlocks[i].getValues()[j][k]);
                    yuvImage.getPixels()[uBlocks[i].getPosX() + 2 * j][uBlocks[i].getPosY() + 2 * k + 1].setU(uBlocks[i].getValues()[j][k]);
                    yuvImage.getPixels()[uBlocks[i].getPosX() + 2 * j + 1][uBlocks[i].getPosY() + 2 * k + 1].setU(uBlocks[i].getValues()[j][k]);
                }
            }
        }

        for (int block = 0; block < vBlocks.length; block++) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    yuvImage.getPixels()[vBlocks[block].getPosX() + 2 * i][vBlocks[block].getPosY() + 2 * j].setV(vBlocks[block].getValues()[i][j]);
                    yuvImage.getPixels()[vBlocks[block].getPosX() + 2 * i + 1][vBlocks[block].getPosY() + 2 * j].setV(vBlocks[block].getValues()[i][j]);
                    yuvImage.getPixels()[vBlocks[block].getPosX() + 2 * i][vBlocks[block].getPosY() + 2 * j + 1].setV(vBlocks[block].getValues()[i][j]);
                    yuvImage.getPixels()[vBlocks[block].getPosX() + 2 * i + 1][vBlocks[block].getPosY() + 2 * j + 1].setV(vBlocks[block].getValues()[i][j]);
                }
            }
        }
        return yuvImage;
    }
}
