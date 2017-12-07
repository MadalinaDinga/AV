public class YUVImage {
    // position in the image
    private int height;
    private int width;
    private YUVPixel[][] pixels;

    public YUVImage() {
    }

    public YUVImage(int height, int width, YUVPixel[][] pixels) {
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

    public YUVPixel[][] getPixels() {
        return pixels;
    }

    public void setPixels(YUVPixel[][] pixels) {
        this.pixels = pixels;
    }
}
