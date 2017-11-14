public class BlockUtils {
    public static Block[] getYBlocks(YUVImage yuvImage) {
        Block[] blocks = new Block[yuvImage.getHeight() / 8 * yuvImage.getWidth() / 8];

        int k = 0;
        for (int i = 0; i < yuvImage.getHeight(); i += 8) {
            for (int j = 0; j < yuvImage.getWidth(); j += 8) {
                Block block = new Block();
                block.setType("Y");
                block.setX(i);
                block.setY(j);
                block.setValues(new Double[8][8]);

                for (int indexI = 0; indexI < 8; indexI++) {
                    for (int indexJ = 0; indexJ < 8; indexJ++) {
                        block.getValues()[indexI][indexJ] = yuvImage.getPixels()[i + indexI][j + indexJ].getY();
                    }
                }
                blocks[k++] = block;
            }
        }
        return blocks;
    }

    public static Block[] getUBlocks(YUVImage yuvImage) {
        Block[] blocks = new Block[yuvImage.getHeight() / 8 * yuvImage.getWidth() / 8];
        int k = 0;
        for (int i = 0; i < yuvImage.getHeight(); i += 8) {
            for (int j = 0; j < yuvImage.getWidth(); j += 8) {
                Block block = new Block();
                block.setType("U");
                block.setX(i);
                block.setY(j);
                block.setValues(new Double[4][4]);

                for (int indexI = 0; indexI < 4; indexI++) {
                    for (int indexJ = 0; indexJ < 4; indexJ++) {
                        Double mean = (yuvImage.getPixels()[i + 2 * indexI][j + 2 * indexJ].getU() +
                                +yuvImage.getPixels()[i + 2 * indexI + 1][j + 2 * indexJ].getU()
                                + yuvImage.getPixels()[i + 2 * indexI][j + 2 * indexJ + 1].getU()
                                + yuvImage.getPixels()[i + 2 * indexI + 1][j + 2 * indexJ + 1].getU()) / 4.;
                        block.getValues()[indexI][indexJ] = mean;
                    }
                }
                blocks[k++] = block;
            }
        }
        return blocks;
    }

    public static Block[] getVlocks(YUVImage yuvImage) {
        Block[] blocks = new Block[yuvImage.getHeight() / 8 * yuvImage.getWidth() / 8];
        int k = 0;
        for (int i = 0; i < yuvImage.getHeight(); i += 8) {
            for (int j = 0; j < yuvImage.getWidth(); j += 8) {
                Block block = new Block();
                block.setType("V");
                block.setX(i);
                block.setY(j);
                block.setValues(new Double[4][4]);

                for (int indexI = 0; indexI < 4; indexI++) {
                    for (int indexJ = 0; indexJ < 4; indexJ++) {
                        Double mean = (yuvImage.getPixels()[i + 2 * indexI][j + 2 * indexJ].getV() +
                                +yuvImage.getPixels()[i + 2 * indexI + 1][j + 2 * indexJ].getV()
                                + yuvImage.getPixels()[i + 2 * indexI][j + 2 * indexJ + 1].getV()
                                + yuvImage.getPixels()[i + 2 * indexI + 1][j + 2 * indexJ + 1].getV()) / 4.;
                        block.getValues()[indexI][indexJ] = mean ;
                    }
                }
                blocks[k++] = block;
            }
        }
        return blocks;
    }

    public static YUVImage getYuvImage(Block[] yBlocks, Block[] uBlocks, Block[] vBlocks) {
        YUVImage yuvImage = new YUVImage();
        yuvImage.setHeight(yBlocks[yBlocks.length - 1].getX() + 8);
        yuvImage.setWidth(yBlocks[yBlocks.length - 1].getY() + 8);
        yuvImage.setPixels(new YUV[yuvImage.getHeight()][yuvImage.getWidth()]);

        for (int i = 0; i < yBlocks.length; i++) {
            for (int k = 0; k < 8; k++) {
                for (int z = 0; z < 8; z++) {
                    YUV yuv = new YUV();
                    yuv.setY(yBlocks[i].getValues()[k][z]);
                    yuvImage.getPixels()[yBlocks[i].getX() + k][yBlocks[i].getY() + z] = yuv;
                }
            }
        }

        for (int i = 0; i < uBlocks.length; i++) {
            for (int k = 0; k < 4; k++) {
                for (int z = 0; z < 4; z++) {
                    yuvImage.getPixels()[uBlocks[i].getX() + 2 * k][uBlocks[i].getY() + 2 * z].setU(uBlocks[i].getValues()[k][z]);
                    yuvImage.getPixels()[uBlocks[i].getX() + 2 * k + 1][uBlocks[i].getY() + 2 * z].setU(uBlocks[i].getValues()[k][z]);
                    yuvImage.getPixels()[uBlocks[i].getX() + 2 * k][uBlocks[i].getY() + 2 * z + 1].setU(uBlocks[i].getValues()[k][z]);
                    yuvImage.getPixels()[uBlocks[i].getX() + 2 * k + 1][uBlocks[i].getY() + 2 * z + 1].setU(uBlocks[i].getValues()[k][z]);
                }
            }
        }

        for (int i = 0; i < vBlocks.length; i++) {
            for (int k = 0; k < 4; k++) {
                for (int z = 0; z < 4; z++) {
                    yuvImage.getPixels()[vBlocks[i].getX() + 2 * k][vBlocks[i].getY() + 2 * z].setV(vBlocks[i].getValues()[k][z]);
                    yuvImage.getPixels()[vBlocks[i].getX() + 2 * k + 1][vBlocks[i].getY() + 2 * z].setV(vBlocks[i].getValues()[k][z]);
                    yuvImage.getPixels()[vBlocks[i].getX() + 2 * k][vBlocks[i].getY() + 2 * z + 1].setV(vBlocks[i].getValues()[k][z]);
                    yuvImage.getPixels()[vBlocks[i].getX() + 2 * k + 1][vBlocks[i].getY() + 2 * z + 1].setV(vBlocks[i].getValues()[k][z]);
                }
            }
        }
        return yuvImage;
    }
}
