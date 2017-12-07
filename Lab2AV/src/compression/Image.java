package compression;

import compression.Block;

import java.util.ArrayList;

/**
 * Created by Madalina Dinga on 07-Dec-17.
 */
public class Image {
    private ArrayList<Short> red;
    private ArrayList<Short> green;
    private ArrayList<Short> blue;

    private ArrayList<Double> y;
    private ArrayList<Double> u;
    private ArrayList<Double> v;

    private ArrayList<Block<Double>> yBlocks;
    private ArrayList<Block<Double>> uBlocks;
    private ArrayList<Block<Double>> vBlocks;

    private ArrayList<Block<Double>> compressedU;
    private ArrayList<Block<Double>> compressedV;

    private ArrayList<Block<Double>> yDCT;
    private ArrayList<Block<Double>> uDCT;
    private ArrayList<Block<Double>> vDCT;

    private ArrayList<Block<Integer>> yQuantized;
    private ArrayList<Block<Integer>> uQuantized;
    private ArrayList<Block<Integer>> vQuantized;

    //metadata
    private String format = "";
    private String comment = "";
    private int width = 0;
    private int height = 0;
    private short maxPixelValue = 0;

    public ArrayList<Block<Double>> getyDCT() {
        return yDCT;
    }

    public void setyDCT(ArrayList<Block<Double>> yDCT) {
        this.yDCT = yDCT;
    }

    public ArrayList<Block<Double>> getuDCT() {
        return uDCT;
    }

    public void setuDCT(ArrayList<Block<Double>> uDCT) {
        this.uDCT = uDCT;
    }

    public ArrayList<Block<Double>> getvDCT() {
        return vDCT;
    }

    public void setvDCT(ArrayList<Block<Double>> vDCT) {
        this.vDCT = vDCT;
    }

    public ArrayList<Block<Integer>> getyQuantized() {
        return yQuantized;
    }

    public void setyQuantized(ArrayList<Block<Integer>> yQuantized) {
        this.yQuantized = yQuantized;
    }

    public ArrayList<Block<Integer>> getuQuantized() {
        return uQuantized;
    }

    public void setuQuantized(ArrayList<Block<Integer>> uQuantized) {
        this.uQuantized = uQuantized;
    }

    public ArrayList<Block<Integer>> getvQuantized() {
        return vQuantized;
    }

    public void setvQuantized(ArrayList<Block<Integer>> vQuantized) {
        this.vQuantized = vQuantized;
    }

    public ArrayList<Short> getRedOnPos() {
        return red;
    }

    public void setRed(ArrayList<Short> red) {
        this.red = red;
    }

    public ArrayList<Short> getGreenOnPos() {
        return green;
    }

    public void setGreen(ArrayList<Short> green) {
        this.green = green;
    }

    public ArrayList<Short> getBlueOnPos() {
        return blue;
    }

    public void setBlue(ArrayList<Short> blue) {
        this.blue = blue;
    }

    public Image() {
        red = new ArrayList<>();
        green = new ArrayList<>();
        blue = new ArrayList<>();

        y = new ArrayList<>();
        u = new ArrayList<>();
        v = new ArrayList<>();

        yBlocks = new ArrayList<>();
        uBlocks = new ArrayList<>();
        vBlocks = new ArrayList<>();

        compressedU = new ArrayList<>();
        compressedV = new ArrayList<>();

        yDCT = new ArrayList<>();
        uDCT = new ArrayList<>();
        vDCT = new ArrayList<>();

        yQuantized = new ArrayList<>();
        uQuantized = new ArrayList<>();
        vQuantized = new ArrayList<>();
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public short getMaxPixelValue() {
        return maxPixelValue;
    }

    public void setMaxPixelValue(short maxPixelValue) {
        this.maxPixelValue = maxPixelValue;
    }

    public ArrayList<Double> getY() {
        return y;
    }

    public void setY(ArrayList<Double> y) {
        this.y = y;
    }

    public ArrayList<Double> getU() {
        return u;
    }

    public void setU(ArrayList<Double> u) {
        this.u = u;
    }

    public ArrayList<Double> getV() {
        return v;
    }

    public void setV(ArrayList<Double> v) {
        this.v = v;
    }

    public void addRed(Short el) {
        red.add(el);
    }
    public void addGreen(Short el) {
        green.add(el);
    }
    public void addBlue(Short el) {
        blue.add(el);
    }

    public Short getRedOnPos(int i) {
        return red.get(i);
    }
    public Short getGreenOnPos(int i) {
        return green.get(i);
    }
    public Short getBlueOnPos(int i) {
        return blue.get(i);
    }

    Double getYOnPos(int i) {
        return y.get(i);
    }
    Double getUOnPos(int i) {
        return u.get(i);
    }
    Double getVOnPos(int i) {
        return v.get(i);
    }

    public void addY(Double el) {
        y.add(el);
    }
    public void addU(Double el) {
        u.add(el);
    }
    public void addV(Double el) {
        v.add(el);
    }

    public void clearYUV(){
        y.clear();
        u.clear();
        v.clear();
    }
    public void clearRGB(){
        red.clear();
        green.clear();
        blue.clear();
    }

    public void clearYUVBlocks(){
        yBlocks.clear();
        uBlocks.clear();
        vBlocks.clear();
    }

    public void clearCompressedBlocks(){
        compressedV.clear();
        compressedU.clear();
    }

    public void addYBlock(Block<Double> blockY){
        yBlocks.add(blockY);
    }
    public void addUBlock(Block<Double> blockU){
        uBlocks.add(blockU);
    }
    public void addVBlock(Block<Double> blockV){
        vBlocks.add(blockV);
    }

    public ArrayList<Block<Double>> getyBlocks() {
        return yBlocks;
    }
    public ArrayList<Block<Double>> getuBlocks() {
        return uBlocks;
    }
    public ArrayList<Block<Double>> getvBlocks() {
        return vBlocks;
    }

    public Block<Double> getYBlockOnPos(int i) {
        return yBlocks.get(i);
    }
    public Block<Double> getUBlockOnPos(int i) {
        return uBlocks.get(i);
    }
    public Block<Double> getVBlockOnPos(int i) {
        return vBlocks.get(i);
    }

    public ArrayList<Block<Double>> getCompressedUBlocks() {
        return compressedU;
    }
    public ArrayList<Block<Double>> getCompressedVBlocks() {
        return compressedV;
    }

    public Block<Double> getCompressedUBlockOnPos(int i) {
        return compressedU.get(i);
    }
    public Block<Double> getCompressedVBlockOnPos(int i) {
        return compressedV.get(i);
    }

    public void addCompressedUBlock(Block<Double> blockU){
        compressedU.add(blockU);
    }
    public void addCompressedVBlock(Block<Double> blockV){
        compressedV.add(blockV);
    }

}