package self.nesl.komicaviewer.model;

import android.graphics.Bitmap;

import self.nesl.komicaviewer.util.UrlUtil;

import static self.nesl.komicaviewer.util.Util.print;

public class Picture {
    private Bitmap bitmap;
    private String originalUrl;
    private String thumbnailUrl;
    private String baseUrl;
    private int thumbWidth;
    private int thumbHeight;
    private int originalWidth;
    private int originalHeight;

    public Picture(String originalUrl, String thumbnailUrl, String baseUrl, int originalWidth, int originalHeight, int thumbWidth, int thumbHeight) {
        this.originalUrl = originalUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.baseUrl = baseUrl;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        this.thumbWidth = thumbWidth == 0 ? 100 : thumbWidth;
        this.thumbHeight = thumbHeight == 0 ? 100 : thumbHeight;
    }

    public String getOriginalUrl() {
        return new UrlUtil(originalUrl, baseUrl).getUrl();
    }

    public String getThumbnailUrl() {
        return new UrlUtil(thumbnailUrl, baseUrl).getUrl();
    }

    public int getOriginalWidth() {
        return originalWidth;
    }

    public int getOriginalHeight() {
        return originalHeight;
    }

    public int getThumbWidth() {
        return thumbWidth;
    }

    public int getThumbHeight() {
        return thumbHeight;
    }
}
