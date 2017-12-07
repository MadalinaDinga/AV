
public class Encoder {
    public static Block[] encodeYBlocks(YUVImage yuvImage) {
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

    public static Block[] encodeUBlocks(YUVImage yuvImage) {
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

    public static Block[] encodeVBlocks(YUVImage yuvImage) {
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
}
