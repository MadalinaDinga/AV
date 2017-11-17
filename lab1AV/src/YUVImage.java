public class YUVImage {
    // position in the image
    private int height;
    private int width;
    private YUV[][] pixels;

    public YUVImage() {
    }

    public YUVImage(int height, int width, YUV[][] pixels) {
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

    public YUV[][] getPixels() {
        return pixels;
    }

    public void setPixels(YUV[][] pixels) {
        this.pixels = pixels;
    }
}
