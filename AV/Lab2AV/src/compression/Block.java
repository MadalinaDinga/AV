package compression;

import java.util.ArrayList;

/**
 * Created by Madalina Dinga on 07-Dec-17.
 */
public class Block<T> {
    private int size;

    private ArrayList<ArrayList<T>> block;

    public Block(int size) {
        this.size = size;
        block = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            block.add(new ArrayList<T>(size));
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<ArrayList<T>> getBlock() {
        return block;
    }

    public void setBlock(ArrayList<ArrayList<T>> block) {
        this.block = block;
    }

    public void addElemOnRowColumn(T elem, int row, int column){
        block.get(row).add(column, elem);
    }

    public void addElem(T elem, int row){
        block.get(row).add(elem);
    }

    public void addRow(ArrayList<T> list, int pos){
        block.set(pos, list);
    }

    public ArrayList<T> getRow(int row){
        return block.get(row);
    }

    public void setElem(T elem, int row, int column) {
        block.get(row).set(column, elem);
    }

    public T getElem(int row, int column){
        return block.get(row).get(column);
    }

    @Override
    public String toString() {
        StringBuilder blockStr = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                blockStr.append(block.get(i).get(j));
                blockStr.append(" ");
            }
            blockStr.append("\n");
        }
        return blockStr.toString();
    }
}
