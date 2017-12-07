package compression;

import java.util.ArrayList;

/**
 * Created by Madalina Dinga on 07-Dec-17.
 */
public class Encoder {
    private Image img;

    public Encoder(Image img) {
        this.img = img;
    }

    public Image getImg() {
        return img;
    }

    /**
     * BLOCK SPLITTING
     * matrix for Y components - blocks of 8x8 values
     * each block:  8x8=64 values/bytes from the block, the type of block (Y) and the position of the block in the image
     * matrix for U components
     * each block: 4x4=16 values/bytes from the block, type(U) and the position in the image
     * matrix for V components
     * each block: 4x4=16 values/bytes from the block, type(U) and the position in the image
     */
    public void splitIntoYUVBlocks(){
        int index = 0;
        img.clearYUVBlocks();

        int size            = 8;
        int processedBlocks = 0;
        int totalBlocks = (img.getWidth() * img.getHeight()) / (size*size);

        while (img.getuBlocks().size() < totalBlocks) {
            Block<Double> blockY = new Block<>(size);
            Block<Double> blockU = new Block<>(size);
            Block<Double> blockV = new Block<>(size);

            int pos = index;

            for (int line = 0; line < 8; line++) {
                //read blocks
                for (int i = pos; i < pos + 8; i++) {
                    //read a block row
                    blockY.addElem(img.getYOnPos(i), line);
                    blockU.addElem(img.getUOnPos(i), line);
                    blockV.addElem(img.getVOnPos(i), line);
                }
                pos += img.getWidth();
            }

            img.addYBlock(blockY);
            img.addUBlock(blockU);
            img.addVBlock(blockV);

            processedBlocks++;

            if (processedBlocks < 100) {
                index += 8;
            } else {
                index += img.getWidth()  * 7;
                index += 8;
                processedBlocks = 0;
            }
        }

        for (int i = 0; i < img.getuBlocks().size(); i++) {
            Block<Double> uBlock = img.getUBlockOnPos(i);
            Block<Double> smallUBlock = new Block<>(4);

            for (int j = 0; j < 8; j+=2) {
                for (int k = 0; k < 8; k+=2) {
                    Double sum = uBlock.getElem(j, k) + uBlock.getElem(j,k+1);
                    sum += uBlock.getElem(j+1, k) + uBlock.getElem(j+1, k+1);
                    smallUBlock.addElem(sum/4, j/2);
                }
            }

            img.getCompressedUBlocks().add(smallUBlock);
        }

        for (int i = 0; i < img.getvBlocks().size(); i++) {
            Block<Double> vBlock = img.getVBlockOnPos(i);
            Block<Double> smallVBlock = new Block<>(4);

            for (int j = 0; j < 8; j+=2) {
                for (int k = 0; k < 8; k+=2) {
                    Double sum = vBlock.getElem(j, k) + vBlock.getElem(j,k+1);
                    sum += vBlock.getElem(j+1, k) + vBlock.getElem(j+1, k+1);
                    smallVBlock.addElem(sum/4, j/2);
                }
            }

            img.getCompressedVBlocks().add(smallVBlock);
        }
        System.out.println("Y Block size: " + img.getyBlocks().get(1).getSize());
        System.out.println("Compressed U Block size: " + img.getCompressedUBlocks().get(1).getSize());
        System.out.println("Compressed V Block size: " + img.getCompressedVBlocks().get(1).getSize());
    }

}
