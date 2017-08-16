package com.hubfly.ctq.Model;

/**
 * Created by Admin on 20-07-2017.
 */

public class ImageModel  {
    String BaseImage,fileName;
    Boolean isServer;

    public Boolean getServer() {
        return isServer;
    }

    public void setServer(Boolean server) {
        isServer = server;
    }

    public String getBaseImage() {
        return BaseImage;
    }

    public void setBaseImage(String baseImage) {
        BaseImage = baseImage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
