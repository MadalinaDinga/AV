public class RGB {
    //COLOR SPACE
    //red
    private double r;
    //green
    private double g;
    //blue
    private double b;

    public RGB() {
    }

    public RGB(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "("+ r +", "+ g +", "+ b +")";
    }
}
