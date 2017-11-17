import static java.lang.StrictMath.round;

public class Block {
    /**
     * 64 values/bytes for Y components.
     * <br/> 16 values/ bytes for U/V components.
     */
    private Double[][] values;
    /**
     * The type of the block.
     */
    private String type;
    /**
     * The position of the block(posX-axis).
     */
    private Integer posX;
    /**
     * The position of the block(posY-axis).
     */
    private Integer posY;


    public Block() {
    }

    public Double[][] getValues() {
        return values;
    }

    public void setValues(Double[][] values) {
        this.values = values;
    }

    public Integer getPosX() {
        return posX;
    }

    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public void setPosY(Integer posY) {
        this.posY = posY;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        String matrix="";
        for(Double[] i :values){
            for(Double c : i){
                matrix+=round(c)+" ";
            }
            matrix+="\n";
        }
        return "Block{" +
                "values={\n"+ matrix +"\n} ,"+
                ", posX=" + posX +
                ", posY=" + posY +
                ", type='" + type + '\'' +
                '}';
    }
}
