public class YUV {
    private double y;
    private double u;
    private double v;

    public YUV() {
    }

    public YUV(double y, double u, double v) {
        this.y = y;
        this.u = u;
        this.v = v;
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
