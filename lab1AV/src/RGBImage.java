public class RGBImage {
    //resolution
    private int height;
    private int width;
    private RGBPixel[][] pixels;

    public RGBImage() {
    }

    public RGBImage(int height, int width, RGBPixel[][] pixels) {
        this.height = height;
        this.width = width;
        this.pixels = pixels;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public RGBPixel[][] getPixels() {
        return pixels;
    }

    public void setPixels(RGBPixel[][] pixels) {
        this.pixels = pixels;
    }
}
