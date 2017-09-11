package dto;

import model.Idiom;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by venkat
 */
public class TopItemDto {

        private String gifId;
        private String baseUrl;
        private String originalFN;
        private String lowResFN;
        private String lowResWebpFN;
        private String thumbNailFN;
        private int width;
        private int height;
        private int lowResWidth;
        private int lowResHeight;
        private String displayName;
        private String text;
        private String exclude;


    public TopItemDto() {
    }

    public TopItemDto(String id, String baseUrl, String originalFN, String lowResFN, String lowResWebpFN, String thumbNailFN) {
        this.gifId = id;
        this.baseUrl = baseUrl;
        this.originalFN = originalFN;
        this.lowResFN = lowResFN;
        this.lowResWebpFN = lowResWebpFN;
        this.thumbNailFN = thumbNailFN;
    }

    public String getGifId() {
        return gifId;
    }

    public void setGifId(String gifId) {
        this.gifId = gifId;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getOriginalFN() {
        return originalFN;
    }

    public void setOriginalFN(String originalFN) {
        this.originalFN = originalFN;
    }

    public String getLowResFN() {
        return lowResFN;
    }

    public void setLowResFN(String lowResFN) {
        this.lowResFN = lowResFN;
    }

    public String getLowResWebpFN() {
        return lowResWebpFN;
    }

    public void setLowResWebpFN(String lowResWebpFN) {
        this.lowResWebpFN = lowResWebpFN;
    }

    public String getThumbNailFN() {
        return thumbNailFN;
    }

    public void setThumbNailFN(String thumbNailFN) {
        this.thumbNailFN = thumbNailFN;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLowResWidth() {
        return lowResWidth;
    }

    public void setLowResWidth(int lowResWidth) {
        this.lowResWidth = lowResWidth;
    }

    public int getLowResHeight() {
        return lowResHeight;
    }

    public void setLowResHeight(int lowResHeight) {
        this.lowResHeight = lowResHeight;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }
}
