package self.nesl.komicaviewer.model;

import android.graphics.Bitmap;

public class Picture {
    private Bitmap bitmap;
    private String originalUrl;
    private String thumbnailUrl;
    private int width;
    private int height;

    public Picture(Bitmap bitmap, String originalUrl, String thumbnailUrl, int width, int height) {
        this.bitmap = bitmap;
        this.originalUrl = originalUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.width = width;
        this.height = height;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
