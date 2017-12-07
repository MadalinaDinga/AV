package compression;

import java.util.ArrayList;

/**
 * Created by Madalina Dinga on 07-Dec-17.
 */
public class Decoder {
    private Image img;

    public Decoder(Image img) {
        this.img = img;
    }

    public Image getImg() {
        return img;
    }

    /**
     * Expand the compressed blocks
     */
    public void restoreImage(){
        img.clearRGB();

        // for DCT - ransform the 4x4 U/V blocks back to 8x8 matrixes, so that a single U/V value is placed in 4 distinct places in the 8x8 matrix
        // (i.e. you do the reverse of subsampling)
        breakCompressedYUVBlocks();

        splitIntoYUVBlocks();
    }

    private void breakCompressedYUVBlocks(){
        img.clearYUV();

        for (int i = 0; i < img.getyBlocks().size(); i+=100) {
            for (int j = 0; j < 8; j++) {
                for (int k = i; k < i + 100; k++) {
                    for (int l = 0; l < 8; l++) {
                        img.getY().add(img.getyBlocks().get(k).getElem(j,l));
                    }
                }
            }
        }

        //expand u and v matrix
        ArrayList<Double> buffer = new ArrayList<>();
        ArrayList<Double> secondaryBuffer = new ArrayList<>();

        for (int i = 0; i < img.getCompressedUBlocks().size(); i+=100) {
            for (int j = 0; j < 4; j++) {
                for (int k = i; k < i + 100; k++) {
                    for (int l = 0; l < 4; l++) {
                        buffer.add(img.getCompressedUBlocks().get(k).getElem(j,l));
                        buffer.add(img.getCompressedUBlocks().get(k).getElem(j,l));
                        secondaryBuffer.add(img.getCompressedUBlocks().get(k).getElem(j,l));
                        secondaryBuffer.add(img.getCompressedUBlocks().get(k).getElem(j,l));
                    }
                }


                buffer.addAll(secondaryBuffer);
                secondaryBuffer.clear();
            }

            img.getU().addAll(buffer);
            buffer.clear();
        }

        buffer.clear();
        secondaryBuffer.clear();


        for (int i = 0; i < img.getCompressedVBlocks().size(); i+=100) {
            for (int j = 0; j < 4; j++) {
                for (int k = i; k < i + 100; k++) {
                    for (int l = 0; l < 4; l++) {
                        buffer.add(img.getCompressedVBlocks().get(k).getElem(j,l));
                        buffer.add(img.getCompressedVBlocks().get(k).getElem(j,l));
                        secondaryBuffer.add(img.getCompressedVBlocks().get(k).getElem(j,l));
                        secondaryBuffer.add(img.getCompressedVBlocks().get(k).getElem(j,l));
                    }
                }

                buffer.addAll(secondaryBuffer);
                secondaryBuffer.clear();
            }

            img.getV().addAll(buffer);
            buffer.clear();
        }
    }

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
