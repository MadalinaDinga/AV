import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Launch {
    /**
     * Launch idea:
     *  1. the first phase of the video encoder: dividing the image into blocks of 8x8 pixels
     *  2. the last phase of the video decoder: composing the image from a set of 8x8 pixels blocks
     */
    public static void main(String[] args) throws InterruptedException {
        //PHASE 1 - dividing the image into blocks of 8x8 pixels
        List<Block> blocks = new ArrayList<>();

        // read the PPM image( RGBPixel)
        RGBImage rgbImage = readPPMImage();

        // FormatConversion each pixel value from RGBPixel to YUVPixel
        YUVImage yuvImage = RGBtoYUV(rgbImage);

        //BLOCK SPLITTING
        // matrix for Y components - blocks of 8x8 values
        // each block:  8x8=64 values/bytes from the block, the type of block (Y) and the position of the block in the image
        Block[] yBlocks = Encoder.encodeYBlocks(yuvImage);

        // matrix for U components
        // each block: 4x4=16 values/bytes from the block, type(U) and the position in the image
        Block[] uBlocks = Encoder.encodeUBlocks(yuvImage);

        // matrix for V components
        Block[] vBlocks = Encoder.encodeVBlocks(yuvImage);

        System.out.println("First Y block:");
        showBlocks(yBlocks);

        System.out.println("First U block:");
        //Thread.sleep(5000);
        showBlocks(uBlocks);

        System.out.println("First V block:");
        //Thread.sleep(2000);
        showBlocks(vBlocks);

        //PHASE 2 - composing the image from a set of 8x8 pixels blocks
        YUVImage decodedYuvImage = Decoder.decodeYUVImage(yBlocks,uBlocks,vBlocks);

        RGBImage decodedRGBImage = YUVtoRGB(decodedYuvImage);

        createPPM(decodedRGBImage);

    }

    private static void showBlocksMatrix(Block[] blocks, int k){
        for(Block block: blocks){
            for (int i=0; i<k; i++) {
                System.out.print(block + " ");
            }
            System.out.println();
        }
    }

    private static void showBlocks(Block[] blocks){
        /*for(Block block: blocks){
                System.out.print(block + " ");
            }*/
        System.out.println(blocks[0]);
    }

    private static void createPPM(RGBImage rgbImage){
        File file = new File("compressed.ppm");
        try {
            //creates a new empty file if a file with this name does not yet exist
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file,false);

            //the HEADER P3, the height, the width( resolution) into a sequence of bytes, storing the result into a new byte array.
            //the format: P3
            outputStream.write("P3\n".getBytes());
            //the resolution
            outputStream.write((rgbImage.getWidth()+" "+rgbImage.getHeight()+"\n").getBytes());
            //the maximum value of a byte component
            outputStream.write((255+"\n").getBytes());

            //the data bytes( 1 pixel = 3 bytes- red, green, blue), storing the result into a new byte array.
            for(int i = 0; i < rgbImage.getHeight(); i++ ){
                for(int j = 0; j < rgbImage.getWidth(); j++){
                    int r = (int) rgbImage.getPixels()[i][j].getR();
                    int g = (int) rgbImage.getPixels()[i][j].getG();
                    int b = (int) rgbImage.getPixels()[i][j].getB();

                    outputStream.write((r+"\n").getBytes());
                    outputStream.write((g+"\n").getBytes());
                    outputStream.write((b+"\n").getBytes());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This file format contains a small header of 3 lines (or more if there are comments - line which start with '#')
     * format - P3
     * resolution( width, height) - 600 x 800
     * maximum value of a byte component - 255
     */
    private static RGBImage readPPMImage() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("nt-P3.ppm")));
            //HEADER
            System.out.println(br.readLine());  //P3
            System.out.println(br.readLine());  // #comment

            String dim = br.readLine();     //800 600
            String[] dims = dim.split(" ");
            Integer height = Integer.valueOf(dims[1]);
            Integer width = Integer.valueOf(dims[0]);
            System.out.println(width + " " + height);

            System.out.println(br.readLine());  //255

            Integer r, g, b;

            RGBImage rgbImage = new RGBImage();
            rgbImage.setHeight(height);
            rgbImage.setWidth(width);
            rgbImage.setPixels(new RGBPixel[height][width]);

            // each pixel gets a red, a green and a blue
            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++) {
                    try {
                        r = Integer.parseInt(br.readLine());
                        g = Integer.parseInt(br.readLine());
                        b = Integer.parseInt(br.readLine());
                        rgbImage.getPixels()[i][j] = new RGBPixel(r, g, b);

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                }
            return rgbImage;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Y = 0.299*R + 0.587*G + 0.114*B
     * U = 128 – 0.1687*R – 0.3312*G + 0.5*B
     * V = 128 + 0.5*R – 0.4186*G – 0.0813*B
     * then the image is pixel padded at right and bottom so that width and height are multiple of 8 (16) bits
     * @param rgbImage
     * @return
     */
    private static YUVImage RGBtoYUV(RGBImage rgbImage) {
        YUVImage yuvImage = new YUVImage();
        yuvImage.setHeight(rgbImage.getHeight());
        yuvImage.setWidth(rgbImage.getWidth());
        yuvImage.setPixels(new YUVPixel[yuvImage.getHeight()][yuvImage.getWidth()]);

        for (int i = 0; i < rgbImage.getHeight(); i++) {
            for (int j = 0; j < rgbImage.getWidth(); j++) {
                YUVPixel yuvPixel = new YUVPixel();
                double y,u,v;

                // https://www.vocal.com/video/rgb-and-yuv-color-space-conversion/
                y = (0.257 * rgbImage.getPixels()[i][j].getR()) + (0.504 * rgbImage.getPixels()[i][j].getG()) +
                        (0.098 * rgbImage.getPixels()[i][j].getB()) + 16;
                u = 128+(-(0.148 *  rgbImage.getPixels()[i][j].getR())) - (0.291 *  rgbImage.getPixels()[i][j].getG()) +
                        (0.439 *  rgbImage.getPixels()[i][j].getB());
                v = 128+(0.439 * rgbImage.getPixels()[i][j].getR()) - (0.368 * rgbImage.getPixels()[i][j].getG()) -
                        (0.071 * rgbImage.getPixels()[i][j].getB());
                yuvPixel.setY(y);
                yuvPixel.setU(u);
                yuvPixel.setV(v);

                yuvImage.getPixels()[i][j] = yuvPixel;
            }
        }
        return yuvImage;
    }

    private static RGBImage YUVtoRGB(YUVImage yuvImage) {
        RGBImage rgbImage = new RGBImage();
        rgbImage.setHeight(yuvImage.getHeight());
        rgbImage.setWidth(yuvImage.getWidth());
        rgbImage.setPixels(new RGBPixel[rgbImage.getHeight()][rgbImage.getWidth()]);

        for (int i = 0; i < rgbImage.getHeight(); i++) {
            for (int j = 0; j < rgbImage.getWidth(); j++) {
                RGBPixel rgbPixel = new RGBPixel();
                double r,g,b;

                //https://www.vocal.com/video/rgb-and-yuv-color-space-conversion/
                r =1.164 * (yuvImage.getPixels()[i][j].getY() - 16.0) + 1.596 * (yuvImage.getPixels()[i][j].getV() - 128.0);
                g =1.164 * (yuvImage.getPixels()[i][j].getY() - 16) - 0.813 *
                        (yuvImage.getPixels()[i][j].getV() - 128.0) - 0.391 * (yuvImage.getPixels()[i][j].getU() - 128);
                b = 1.164 * (yuvImage.getPixels()[i][j].getY() - 16.0) + 2.018 * (yuvImage.getPixels()[i][j].getU() - 128.0);

                if (r>255){
                    r=255;
                }if (g>255){
                    g=255;
                }if (b>255){
                    b=255;
                }
                rgbPixel.setR(r);
                rgbPixel.setG(g);
                rgbPixel.setB(b);
                rgbImage.getPixels()[i][j] = rgbPixel;
            }
        }
        return rgbImage;
    }
}
