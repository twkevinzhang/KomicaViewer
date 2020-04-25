package self.nesl.komicaviewer.model;

import android.graphics.Bitmap;

public class Picture {
    private Bitmap bitmap;
    private String originalUrl;
    private String thumbnailUrl;
    private int thumbWidth;
    private int thumbHeight;
    private int originalWidth;
    private int originalHeight;

    public Picture(Bitmap bitmap, String originalUrl, String thumbnailUrl, int originalWidth, int originalHeight, int thumbWidth, int thumbHeight) {
        this.bitmap = bitmap;
        this.originalUrl = originalUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        this.thumbWidth = thumbWidth;
        this.thumbHeight = thumbHeight;
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

    public int getOriginalWidth() {
        return originalWidth;
    }

    public int getOriginalHeight() {
        return originalHeight;
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

    public void setOriginalWidth(int originalWidth) {
        this.originalWidth = originalWidth;
    }

    public void setOriginalHeight(int originalHeight) {
        this.originalHeight = originalHeight;
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public int getThumbHeight() {
        return thumbHeight;
    }

    public void setThumbWidth(int thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public void setThumbHeight(int thumbHeight) {
        this.thumbHeight = thumbHeight;
    }
}
