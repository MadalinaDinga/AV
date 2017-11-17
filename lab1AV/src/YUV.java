public class YUV {
    //COLOR SPACE
    //luminance
    private double y;
    //blue chrominance
    private double u;
    //red chrominance
    private double v;

    public YUV() {
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getU() {
        return u;
    }

    public void setU(double u) {
        this.u = u;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return "("+y+", "+u+", "+v+")";
    }
}
