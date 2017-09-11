package model;

import dto.GifConversionStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by venkat
 */

@Document
public class GifConversion {
    @Id
    private String id;
    private String gifId;
    private GifConversionStatus lowResGifConversionStatus;
    private GifConversionStatus lowResWebpGifConversionStatus;
    private GifConversionStatus thumbNailGifConversionStatus;
    private GifConversionStatus waterMarkedGifConversionStatus;
    private String lowResGifConversionMessage;
    private String lowResWebpGifConversionMessage;
    private String thumbNailGifConversionMessage;
    private String waterMarkedGifConversionMessage;


    public GifConversion(String gifId) {
        this.gifId = gifId;
    }

    public GifConversion() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGifId() {
        return gifId;
    }

    public void setGifId(String gifId) {
        this.gifId = gifId;
    }

    public GifConversionStatus getLowResGifConversionStatus() {
        return lowResGifConversionStatus;
    }

    public void setLowResGifConversionStatus(GifConversionStatus lowResGifConversionStatus) {
        this.lowResGifConversionStatus = lowResGifConversionStatus;
    }

    public GifConversionStatus getLowResWebpGifConversionStatus() {
        return lowResWebpGifConversionStatus;
    }

    public void setLowResWebpGifConversionStatus(GifConversionStatus lowResWebpGifConversionStatus) {
        this.lowResWebpGifConversionStatus = lowResWebpGifConversionStatus;
    }

    public GifConversionStatus getThumbNailGifConversionStatus() {
        return thumbNailGifConversionStatus;
    }

    public void setThumbNailGifConversionStatus(GifConversionStatus thumbNailGifConversionStatus) {
        this.thumbNailGifConversionStatus = thumbNailGifConversionStatus;
    }

    public GifConversionStatus getWaterMarkedGifConversionStatus() {
        return waterMarkedGifConversionStatus;
    }

    public void setWaterMarkedGifConversionStatus(GifConversionStatus waterMarkedGifConversionStatus) {
        this.waterMarkedGifConversionStatus = waterMarkedGifConversionStatus;
    }

    public String getLowResGifConversionMessage() {
        return lowResGifConversionMessage;
    }

    public void setLowResGifConversionMessage(String lowResGifConversionMessage) {
        this.lowResGifConversionMessage = lowResGifConversionMessage;
    }

    public String getLowResWebpGifConversionMessage() {
        return lowResWebpGifConversionMessage;
    }

    public void setLowResWebpGifConversionMessage(String lowResWebpGifConversionMessage) {
        this.lowResWebpGifConversionMessage = lowResWebpGifConversionMessage;
    }

    public String getThumbNailGifConversionMessage() {
        return thumbNailGifConversionMessage;
    }

    public void setThumbNailGifConversionMessage(String thumbNailGifConversionMessage) {
        this.thumbNailGifConversionMessage = thumbNailGifConversionMessage;
    }

    public String getWaterMarkedGifConversionMessage() {
        return waterMarkedGifConversionMessage;
    }

    public void setWaterMarkedGifConversionMessage(String waterMarkedGifConversionMessage) {
        this.waterMarkedGifConversionMessage = waterMarkedGifConversionMessage;
    }
}
