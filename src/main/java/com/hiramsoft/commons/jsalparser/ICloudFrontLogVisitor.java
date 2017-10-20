package com.hiramsoft.commons.jsalparser;

/**
 * Created by ihiram on 8/28/14.
 */
public interface ICloudFrontLogVisitor {
    void accept(CloudFrontWebLogEntry entry);
}
