import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Block> blocks = new ArrayList<>();

        RGBImage rgbImage = readRgbImage();

        YUVImage yuvImage = RgbToYuv.convert(rgbImage);

        Block[] yBlocks = BlockUtils.getYBlocks(yuvImage);

        Block[] uBlocks = BlockUtils.getUBlocks(yuvImage);

        Block[] vBlocks = BlockUtils.getVlocks(yuvImage);

        showBlocks(yBlocks);
        showBlocks(uBlocks);
        showBlocks(vBlocks);

        YUVImage decodedYuvImage = BlockUtils.getYuvImage(yBlocks,uBlocks,vBlocks);

        RGBImage decodedRGBImage = RgbToYuv.convertToRGB(decodedYuvImage);

        createPPM(decodedRGBImage);

    }

    private static void showBlocks(Block[] blocks){
        for(Block block: blocks){
            System.out.print(block+" ");
        }
        System.out.println();
    }

    private static void createPPM(RGBImage rgbImage){
        File file = new File("result.ppm");
        try {
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file,false);

            outputStream.write("P3\n".getBytes());
            outputStream.write("#Some comment\n".getBytes());
            outputStream.write((rgbImage.getHeight()+" "+rgbImage.getWidth()+"\n").getBytes());
            outputStream.write((255+"\n").getBytes());

            for(int i = 0; i < rgbImage.getHeight(); i++ ){
                for(int j = 0; j < rgbImage.getWidth(); j++){
                    int red = (int) rgbImage.getPixels()[i][j].getRed();
                    int green = (int) rgbImage.getPixels()[i][j].getGreen();
                    int blue = (int) rgbImage.getPixels()[i][j].getBlue();

                    outputStream.write((red+"\n").getBytes());
                    outputStream.write((green+"\n").getBytes());
                    outputStream.write((blue+"\n").getBytes());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static RGBImage readRgbImage() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("nt-P3.ppm")));
            System.out.println(br.readLine());
            System.out.println(br.readLine());
            String dimenssions = br.readLine();
            String[] dims = dimenssions.split(" ");
            System.out.println(br.readLine());

            Integer width = Integer.valueOf(dims[1]);
            Integer height = Integer.valueOf(dims[0]);

            Integer r, g, b;

            System.out.println(width + " " + height);
            RGBImage rgbImage = new RGBImage();
            rgbImage.setHeight(height);
            rgbImage.setWidth(width);
            rgbImage.setPixels(new RGB[height][width]);

            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++) {
                    try {
                        r = Integer.parseInt(br.readLine());
                        g = Integer.parseInt(br.readLine());
                        b = Integer.parseInt(br.readLine());
                        rgbImage.getPixels()[i][j] = new RGB(r, g, b);

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                }
            return rgbImage;
        } catch (Exception e) {
            return null;
        }

    }
}
