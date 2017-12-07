import compression.Decoder;
import compression.Encoder;
import compression.FDCTQuantization;
import compression.Image;

/**
 * Created by Madalina Dinga on 07-Dec-17.
 */
public class Main {
    public static void main(String[] args) {
        Image img = new Image();
        Utils.readPPMImage("nt-P3.ppm", img);

        // ENCODER
        Encoder encoder = new Encoder(img);
        // YCbCr conversion
        Utils.fromRGBToYUV(encoder.getImg());

        // lab 1- split into 8*8/ 4*4 blocks( subsampling 4:2:0)
        encoder.splitIntoYUVBlocks();

        //  write compressed image into a file
        Utils.writeCompressedImage("cout.txt", encoder.getImg());

        Image restoredImage = new Image();

        // read the image from file in compressed format
        Utils.readCompressedImage("cout.txt", restoredImage);

        // DECODER
        Decoder decoder = new Decoder(restoredImage);

        // lab1 - composing the image from a set of 8x8 pixels blocks
        // for DCT - transform the 4x4 U/V blocks back to 8x8 matrixes, so that a single U/V value is placed in 4 distinct places in the 8x8 matrix
        // (i.e. you do the reverse of subsampling)
        decoder.restoreImage();

        // lab 2 - phase 2 - DCT + Quantization
        FDCTQuantization fdctQuantization = new FDCTQuantization(decoder.getImg());
        fdctQuantization.applyFDCT();
        fdctQuantization.applyQuantization();

        // lab 2 - dequantization + inverse DCT
        fdctQuantization.applyDeQuantization();
        fdctQuantization.applyIDCT();

        // RGB conversion
        Utils.fromYUVToRGB(decoder.getImg());

        Utils.writePPMImage("result.ppm", fdctQuantization.getImg());
    }
}
