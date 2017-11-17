public class RGBImage {
    //resolution
    private int height;
    private int width;
    private RGB[][] pixels;

    public RGBImage() {
    }

    public RGBImage(int height, int width, RGB[][] pixels) {
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

    public RGB[][] getPixels() {
        return pixels;
    }

    public void setPixels(RGB[][] pixels) {
        this.pixels = pixels;
    }
}
