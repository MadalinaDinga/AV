import java.util.Arrays;

public class Block {
    private Double[][] values;
    private Integer x;
    private Integer y;
    private String type;

    public Block() {
    }

    public Block(Double[][] values, Integer x, Integer y, String type) {
        this.values = values;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public Double[][] getValues() {
        return values;
    }

    public void setValues(Double[][] values) {
        this.values = values;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
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
        for(Double[] rows :values){
            for(Double elem : rows){
                matrix+=elem+";";
            }
        }
        return "Block{" +
                "values={"+"} ,"+
                ", x=" + x +
                ", y=" + y +
                ", type='" + type + '\'' +
                '}';
    }
}
