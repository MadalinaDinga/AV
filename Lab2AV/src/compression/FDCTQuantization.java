package compression;

/**
 * Created by Madalina Dinga on 07-Dec-17.
 */
public class FDCTQuantization {
    private Image img;

    public FDCTQuantization(Image img) {
        this.img = img;
    }

    public Image getImg() {
        return img;
    }

    /**
     * alfa = 1/sqrt(2) if u=0
     *        1 if u > 0.
     */
    private double alfa(int u){
        if (u == 0){
            return 1/Math.sqrt(2);
        }
        else return 1;
    }

    /**
     * Forward DCT (Discrete Cosine Transform) takes as input an 8x8 Y/Cb/Cr values block
     * It transforms this block intro another 8x8 DCT coefficient block.
     */
    public void applyFDCT(){
        img.getyDCT().clear();
        img.getuDCT().clear();
        img.getvDCT().clear();

        for (int i = 0; i < img.getyBlocks().size(); i++) {
            Block<Double> yDCTBlock = new Block<>(8);
            Block<Double> yCurrentBlock = img.getyBlocks().get(i);

            Block<Double> uDCTBlock = new Block<>(8);
            Block<Double> uCurrentBlock = img.getuBlocks().get(i);

            Block<Double> vDCTBlock = new Block<>(8);
            Block<Double> vCurrentBlock = img.getvBlocks().get(i);

            // the final value = the DCT coefficient from coordinates "u" and "v" in the resulting 8x8 DCT block
            for (int u = 0; u < 8; u++) {
                for (int v = 0; v < 8; v++) {
                    double ySum = 0;
                    double uSum = 0;
                    double vSum = 0;

                    // Y/Cb/Cr value from coordinates "x" and "y" in the input 8x8 Y/Cb/Cr block
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            double DCTCoeficient = Math.cos(((2*x + 1)*u*Math.PI)/16) * Math.cos(((2*y + 1)*v*Math.PI)/16);

                            //substract 128 from each value of every 8x8 Y/U/V block
                            ySum += (yCurrentBlock.getElem(x,y) - 128) * DCTCoeficient;
                            uSum += (uCurrentBlock.getElem(x,y) - 128) * DCTCoeficient;
                            vSum += (vCurrentBlock.getElem(x,y) - 128) * DCTCoeficient;
                        }
                    }

                    double yFinalDCTValue = ((double) 1/4) * alfa(u) * alfa(v) * ySum;
                    double uFinalDCTValue = ((double) 1/4) * alfa(u) * alfa(v) * uSum;
                    double vFinalDCTValue = ((double) 1/4) * alfa(u) * alfa(v) * vSum;

                    yDCTBlock.addElem(yFinalDCTValue, u);
                    uDCTBlock.addElem(uFinalDCTValue, u);
                    vDCTBlock.addElem(vFinalDCTValue, u);
                }
            }

            img.getyDCT().add(yDCTBlock);
            img.getuDCT().add(uDCTBlock);
            img.getvDCT().add(vDCTBlock);
        }
    }

    /**
     * Quantization phase takes as input an 8x8 block of DCT coefficient
     * It divides this block to an 8x8 quantization matrix obtaining an 8x8 quantized coefficients block.
     */
    public void applyQuantization() {
        img.getyQuantized().clear();
        img.getuQuantized().clear();
        img.getvQuantized().clear();

        int[][] quantizationMatrix = {
                {6, 4, 4, 6, 10, 16, 20, 24},
                {5, 5, 6, 8, 10, 23, 24, 22},
                {6, 5, 6, 10, 16, 23, 28, 22},
                {6, 7, 9, 12, 20, 35, 32, 25},
                {7, 9, 15, 22, 27, 44, 41, 31},
                {10, 14, 22, 26, 32, 42, 45, 37},
                {20, 26, 31, 35, 41, 48, 48, 40},
                {29, 37, 38, 39, 45, 40, 41, 40}
        };

        for (int i = 0; i < img.getyDCT().size(); i++) {
            Block<Double> yCurrentBlock = img.getyDCT().get(i);
            Block<Double> uCurrentBlock = img.getyDCT().get(i);
            Block<Double> vCurrentBlock = img.getyDCT().get(i);

            Block<Integer> yQuantizedBlock = new Block<>(8);
            Block<Integer> uQuantizedBlock = new Block<>(8);
            Block<Integer> vQuantizedBlock = new Block<>(8);

            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    // The division is performed component-wise (i.e. DCT[x][y] is divided to Q[x][y])
                    // integer division - keep only the quotient, loose the remainder
                    yQuantizedBlock.addElem((int) (yCurrentBlock.getElem(x, y) / quantizationMatrix[x][y]), x);
                    uQuantizedBlock.addElem((int) (uCurrentBlock.getElem(x, y) / quantizationMatrix[x][y]), x);
                    vQuantizedBlock.addElem((int) (vCurrentBlock.getElem(x, y) / quantizationMatrix[x][y]), x);
                }
            }

            img.getyQuantized().add(yQuantizedBlock);
            img.getuQuantized().add(uQuantizedBlock);
            img.getvQuantized().add(vQuantizedBlock);
        }
    }

    /**
     * DeQuantization akes as input an 8x8 quantized block produced by the encoder.
     * It multiplies this block (component-by-component) with the 8x8 quantization matrix outlined above.
     */
    public void applyDeQuantization(){
        img.getyDCT().clear();
        img.getuDCT().clear();
        img.getvDCT().clear();

        int[][] quantizationMatrix = {
                {6,   4,   4,  6,   10,  16,  20,  24},
                {5,   5,   6,  8,   10,  23,  24,  22},
                {6,   5,   6,  10,  16,  23,  28,  22},
                {6,   7,   9,  12,  20,  35,  32,  25},
                {7,   9,  15,  22,  27,  44,  41,  31},
                {10, 14,  22,  26,  32,  42,  45,  37},
                {20, 26,  31,  35,  41,  48,  48,  40},
                {29, 37,  38,  39,  45,  40,  41,  40}
        };

        for (int i = 0; i < img.getyQuantized().size(); i++) {
            Block<Integer> yCurrentBlock = img.getyQuantized().get(i);
            Block<Integer> uCurrentBlock = img.getuQuantized().get(i);
            Block<Integer> vCurrentBlock = img.getvQuantized().get(i);

            Block<Double> yDCTBlock = new Block<>(8);
            Block<Double> uDCTBlock = new Block<>(8);
            Block<Double> vDCTBlock = new Block<>(8);

            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    yDCTBlock.addElem((double) yCurrentBlock.getElem(x,y) * quantizationMatrix[x][y], x);
                    uDCTBlock.addElem((double) uCurrentBlock.getElem(x,y) * quantizationMatrix[x][y], x);
                    vDCTBlock.addElem((double) vCurrentBlock.getElem(x,y) * quantizationMatrix[x][y], x);
                }
            }

            img.getyDCT().add(yDCTBlock);
            img.getuDCT().add(uDCTBlock);
            img.getvDCT().add(vDCTBlock);
        }
    }

    /**
     * Inverse DCT (Discrete Cosine Transform) is the opposite of Forward DCT used by the encoder;
     * It takes a 8x8 DCT coefficients block and it produces an 8x8 Y/Cb/Cr block.
     */
    public void applyIDCT(){
        img.clearYUVBlocks();

        for (int i = 0; i < img.getyDCT().size(); i++) {
            Block<Double> yCurrentBlock = img.getyDCT().get(i);
            Block<Double> uCurrentBlock = img.getuDCT().get(i);
            Block<Double> vCurrentBlock = img.getvDCT().get(i);

            Block<Double> yIDCTBlock = new Block<>(8);
            Block<Double> uIDCTBlock = new Block<>(8);
            Block<Double> vIDCTBlock = new Block<>(8);

            //the Y/Cb/Cr value from coordinates "x" and "y" in the resulting 8x8 Y/Cb/Cr block
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    double ySum = 0;
                    double uSum = 0;
                    double vSum = 0;

                    //the DCT coefficient from coordinates "u" and "v" in the input 8x8 DCT block
                    for (int u = 0; u < 8; u++) {
                        for (int v = 0; v < 8; v++) {
                            double commonPart = alfa(u) * alfa(v) * Math.cos(((2*x + 1)*u*Math.PI)/16) * Math.cos(((2*y + 1)*v*Math.PI)/16);

                            ySum += commonPart * yCurrentBlock.getElem(u,v);
                            uSum += commonPart * uCurrentBlock.getElem(u,v);
                            vSum += commonPart * vCurrentBlock.getElem(u,v);
                        }
                    }

                    //add 128 to each value of every 8x8 Y/Cb/Cr block obtained
                    yIDCTBlock.addElem((0.25 * ySum) + 128, x);
                    uIDCTBlock.addElem((0.25 * uSum) + 128, x);
                    vIDCTBlock.addElem((0.25 * vSum) + 128, x);
                }
            }

            img.getyBlocks().add(yIDCTBlock);
            img.getuBlocks().add(uIDCTBlock);
            img.getvBlocks().add(vIDCTBlock);
        }
    }
}
