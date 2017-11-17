public class BuildBlocks {
    public static Block[] getYBlocks(YUVImage yuvImage) {
        Block[] blocks = new Block[yuvImage.getHeight() / 8 * yuvImage.getWidth() / 8];

        int k = 0;
        for (int i = 0; i < yuvImage.getHeight(); i += 8) {
            for (int j = 0; j < yuvImage.getWidth(); j += 8) {
                Block block = new Block();
                block.setType("Y");
                block.setPosX(i);
                block.setPosY(j);
                block.setValues(new Double[8][8]);

                //set block values
                for (int blockI = 0; blockI < 8; blockI++) {
                    for (int blockJ = 0; blockJ < 8; blockJ++) {
                        block.getValues()[blockI][blockJ] = yuvImage.getPixels()[i + blockI][j + blockJ].getY();
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
                block.setPosX(i);
                block.setPosY(j);
                block.setValues(new Double[4][4]);

                //perform 4:2:0 subsampling, that is for each 2x2 U/V values store only one U/V value which should be the average of those 2x2=4 values
                for (int blockI = 0; blockI < 4; blockI++) {
                    for (int blockJ = 0; blockJ < 4; blockJ++) {
                        Double mean = (yuvImage.getPixels()[i + 2 * blockI][j + 2 * blockJ].getU() +
                                +yuvImage.getPixels()[i + 2 * blockI + 1][j + 2 * blockJ].getU()
                                + yuvImage.getPixels()[i + 2 * blockI][j + 2 * blockJ + 1].getU()
                                + yuvImage.getPixels()[i + 2 * blockI + 1][j + 2 * blockJ + 1].getU()) / 4.;
                        block.getValues()[blockI][blockJ] = mean;
                    }
                }
                blocks[k++] = block;
            }
        }
        return blocks;
    }

    public static Block[] getVBlocks(YUVImage yuvImage) {
        Block[] blocks = new Block[yuvImage.getHeight() / 8 * yuvImage.getWidth() / 8];
        int k = 0;
        for (int i = 0; i < yuvImage.getHeight(); i += 8) {
            for (int j = 0; j < yuvImage.getWidth(); j += 8) {
                Block block = new Block();
                block.setType("V");
                block.setPosX(i);
                block.setPosY(j);
                block.setValues(new Double[4][4]);

                //perform 4:2:0 subsampling, that is for each 2x2 U/V values store only one U/V value which should be the average of those 2x2=4 values
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

    public static YUVImage getYUVImage(Block[] yBlocks, Block[] uBlocks, Block[] vBlocks) {
        YUVImage yuvImage = new YUVImage();
        yuvImage.setHeight(yBlocks[yBlocks.length - 1].getPosX() + 8);
        yuvImage.setWidth(yBlocks[yBlocks.length - 1].getPosY() + 8);
        yuvImage.setPixels(new YUV[yuvImage.getHeight()][yuvImage.getWidth()]);

        for (int i = 0; i < yBlocks.length; i++) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    YUV yuv = new YUV();
                    yuv.setY(yBlocks[i].getValues()[j][k]);
                    yuvImage.getPixels()[yBlocks[i].getPosX() + j][yBlocks[i].getPosY() + k] = yuv;
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
