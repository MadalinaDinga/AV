import compression.Block;
import compression.Image;

import java.io.*;
import java.util.Objects;

/**
 * Created by Madalina Dinga on 07-Dec-17.
 */
public class Utils {
    /**
     * PPM - This file format contains a small header of 3 lines (or more if there are comments - line which start with '#')
     * format - P3
     * resolution( width, height) - 600 x 800
     * maximum value of a byte component - 255
     */
    public static void readPPMImage(String filename, Image img){
        String line;

        byte row = 1;

        File photo = new File(filename);
        try {
            FileReader fileReader = new FileReader(photo);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            StringBuilder comment = new StringBuilder();

            while (row < 4){
                line = bufferedReader.readLine();

                if (line.startsWith("#")) {
                    comment.append(line).append("\n");
                } else {
                    switch (row) {
                        case 1:
                            img.setFormat(line);
                            row++;
                            break;
                        case 2:
                            String[] resolution = line.split(" ");
                            img.setWidth(Integer.parseInt(resolution[0]));
                            img.setHeight(Integer.parseInt(resolution[1]));
                            row++;
                            break;
                        case 3:
                            img.setMaxPixelValue(Short.parseShort(line));
                            row++;
                            break;
                        default:
                            throw new Exception("Outpaced in reading photo metadata");
                    }
                }
            }

            img.setComment(comment.toString());

            row = 1;

            while ((line = bufferedReader.readLine()) != null) {
                switch (row) {
                    case 1:
                        img.addRed(Short.parseShort(line));
                        row++;
                        break;
                    case 2:
                        img.addGreen(Short.parseShort(line));
                        row++;
                        break;
                    case 3:
                        img.addBlue(Short.parseShort(line));
                        row = 1;
                        break;
                    default:
                        throw new Exception("Outpaced in reading photo data");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeCompressedImage(String filename, Image img){
        File newFile = new File(filename);
        try {
            FileWriter fileWriter = new FileWriter(newFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //write metadata
            bufferedWriter.write(img.getFormat());
            bufferedWriter.newLine();
            bufferedWriter.write(img.getComment());
//            bufferedWriter.newLine();
            bufferedWriter.write(img.getWidth() + " " + img.getHeight());
            bufferedWriter.newLine();
            bufferedWriter.write(img.getMaxPixelValue() + "");
            bufferedWriter.newLine();

            bufferedWriter.write("-Y");
            bufferedWriter.newLine();
//            for (Double aY : y) {
//                bufferedWriter.write(aY.toString());
//                bufferedWriter.newLine();
//            }
            for (int i = 0; i < img.getyBlocks().size(); i++) {
                Block<Double> block = img.getYBlockOnPos(i);

                for (int j = 0; j < block.getSize(); j++) {
                    for (int k = 0; k < block.getSize(); k++) {
                        bufferedWriter.write(block.getElem(j,k).toString());
                        bufferedWriter.newLine();
                    }
                }
            }

            bufferedWriter.write("-U");
            bufferedWriter.newLine();

//            for (int i = 0; i < sCompressedU.size(); i++) {
//                bufferedWriter.write(sCompressedU.get(i).toString());
//                bufferedWriter.newLine();
//            }

            for (int i = 0; i < img.getCompressedUBlocks().size(); i++) {
                Block<Double> block = img.getCompressedUBlockOnPos(i);

                for (int j = 0; j < block.getSize(); j++) {
                    for (int k = 0; k < block.getSize(); k++) {
                        bufferedWriter.write(block.getElem(j,k).toString());
                        bufferedWriter.newLine();
                    }
                }
            }

            bufferedWriter.write("-V");
            bufferedWriter.newLine();
//            for (int i = 0; i < sCompressedV.size(); i++) {
//                bufferedWriter.write(sCompressedV.get(i).toString());
//                bufferedWriter.newLine();
//            }

            for (int i = 0; i < img.getCompressedVBlocks().size(); i++) {
                Block<Double> block = img.getCompressedVBlockOnPos(i);

                for (int j = 0; j < block.getSize(); j++) {
                    for (int k = 0; k < block.getSize(); k++) {
                        bufferedWriter.write(block.getElem(j,k).toString());
                        bufferedWriter.newLine();
                    }
                }
            }
            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readCompressedImage(String filename, Image img){
        img.clearYUVBlocks();
        img.clearCompressedBlocks();

        String line;

        byte pace = 1;

        File photo = new File(filename);
        try {
            FileReader fileReader = new FileReader(photo);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuffer commentStr = new StringBuffer();

            while (pace < 4){
                line = bufferedReader.readLine();

                if (line.startsWith("#")) {
                    commentStr.append(line).append("\n");
                } else {
                    switch (pace) {
                        case 1:
                            img.setFormat(line);
                            pace++;
                            break;
                        case 2:
                            String[] resolution = line.split(" ");
                            img.setWidth(Integer.parseInt(resolution[0]));
                            img.setHeight(Integer.parseInt(resolution[1]));
                            pace++;
                            break;
                        case 3:
                            img.setMaxPixelValue(Short.parseShort(line));
                            pace++;
                            break;
                        default:
                            throw new Exception("Outpaced in reading photo metadata");
                    }
                }
            }

            String yMark = bufferedReader.readLine();
            if (!Objects.equals(yMark, "-Y")) throw new Exception("Did not encounter -Y tag!");


            int row = 0;
            int column = 0;
            Block<Double> block = new Block<>(8);
            while (!Objects.equals(line = bufferedReader.readLine(), "-U") && line != null) {
//                y.add(Double.parseDouble(line));
                block.addElem(Double.parseDouble(line), row);
                if (row == 7 && column == 7){
                    row = 0;
                    column = 0;
                    img.addYBlock(block);
                    block = new Block<>(8);
                } else if (column == 7){
                    row += 1;
                    column = 0;
                } else {
                    column++;
                }
            }

            if (!Objects.equals(line, "-U")) throw new Exception("Did not encounter -U tag!");

            row = 0;
            column = 0;
            block = new Block<>(4);
            Block<Double> expandedBlock = new Block<>(8);
            int expandedRow = 0;
            while (!Objects.equals(line = bufferedReader.readLine(), "-V") && line != null) {
//                sCompressedU.add(Double.parseDouble(line));
                block.addElem(Double.parseDouble(line), row);
                expandedBlock.addElem(Double.parseDouble(line), expandedRow);
                expandedBlock.addElem(Double.parseDouble(line), expandedRow);
                if (row == 3 && column == 3){

                    row = 0;
                    column = 0;
                    expandedRow = 0;

                    img.addCompressedUBlock(block);
                    block = new Block<>(4);

                    img.addUBlock(expandedBlock);
                    expandedBlock = new Block<>(8);
                } else if (column == 3){
                    expandedBlock.addRow(expandedBlock.getRow(expandedRow), expandedRow+1);
                    expandedRow += 2;

                    row += 1;
                    column = 0;
                } else {
                    column++;
                }
            }

            if (!Objects.equals(line, "-V")) throw new Exception("Did not encounter -V tag!");

            block = new Block<>(4);
            row = 0;
            column = 0;

            expandedBlock = new Block<>(8);
            expandedRow = 0;
            while ((line = bufferedReader.readLine()) != null) {
//                sCompressedV.add(Double.parseDouble(line));
                block.addElem(Double.parseDouble(line), row);
                expandedBlock.addElem(Double.parseDouble(line), expandedRow);
                expandedBlock.addElem(Double.parseDouble(line), expandedRow);
                if (row == 3 && column == 3){
                    row = 0;
                    column = 0;
                    img.addCompressedVBlock(block);
                    block = new Block<>(4);

                    img.addVBlock(expandedBlock);
                    expandedBlock = new Block<>(8);
                    expandedRow = 0;
                } else if (column == 3){
                    expandedBlock.addRow(expandedBlock.getRow(expandedRow), expandedRow+1);
                    expandedRow += 2;

                    row += 1;
                    column = 0;
                } else {
                    column++;
                }
            }

            img.getuBlocks().clear();
            img.getvBlocks().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void writePPMImage(String filename, Image img){
        File newFile = new File(filename);
        try {
            FileWriter fileWriter = new FileWriter(newFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            //write metadata
            bufferedWriter.write(img.getFormat());
            bufferedWriter.newLine();
            bufferedWriter.write(img.getComment());
//            bufferedWriter.newLine();
            bufferedWriter.write(img.getWidth() + " " + img.getHeight());
            bufferedWriter.newLine();
            bufferedWriter.write(img.getMaxPixelValue() + "");
            bufferedWriter.newLine();

            for (int i = 0; i < img.getRedOnPos().size(); i++) {

                bufferedWriter.write(img.getRedOnPos().get(i).toString());
                bufferedWriter.newLine();
                bufferedWriter.write(img.getGreenOnPos().get(i).toString());
                bufferedWriter.newLine();
                bufferedWriter.write(img.getBlueOnPos().get(i).toString());
                bufferedWriter.newLine();
            }

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ensure that the maximum value remains 255.
     */
    private static short clamp(long value){
        return value > 255 ? 255 : (value < 0 ? 0 : (short) value);
    }

    public static void fromYUVToRGB(Image img){
        for (int i = 0; i < img.getY().size(); i++) {
//            red.add(clamp(Math.round(255 + 1.402 * (v.get(i) - 128))));
//            green.add(clamp(Math.round(255 - 0.344 * (u.get(i) - 128) - 0.714 * (v.get(i) - 128))));
//            blue.add(clamp(Math.round(255 + 1.772 * (u.get(i) - 128))));
            img.addRed(clamp(Math.round(img.getY().get(i) + 1.402 * (img.getV().get(i) - 128))));
            img.addGreen(clamp(Math.round(img.getY().get(i) - 0.344 * (img.getU().get(i) - 128) - 0.714 * (img.getV().get(i) - 128))));
            img.addBlue(clamp(Math.round(img.getY().get(i) + 1.772 * (img.getU().get(i) - 128))));
        }
    }

    /**
     * FormatConversion each pixel value from RGBPixel to YUVPixel
     * Y = 0.299*R + 0.587*G + 0.114*B
     * U = 128 – 0.1687*R – 0.3312*G + 0.5*B
     * V = 128 + 0.5*R – 0.4186*G – 0.0813*B
     * then the image is pixel padded at right and bottom so that width and height are multiple of 8 (16) bits
     */
    public static void fromRGBToYUV(Image img){
        img.clearYUV();

        for (int i = 0; i < img.getRedOnPos().size(); i++) {
            img.addY(0.299 * img.getRedOnPos(i) + 0.587 * img.getGreenOnPos(i) + 0.114 * img.getBlueOnPos(i));
            img.addU(128 - 0.1687 * img.getRedOnPos(i) - 0.3312 * img.getGreenOnPos(i) + 0.5 * img.getBlueOnPos(i));
            img.addV(128 + 0.5 * img.getRedOnPos(i) - 0.4186 * img.getGreenOnPos(i) - 0.0813 * img.getBlueOnPos(i));
        }
    }

}
