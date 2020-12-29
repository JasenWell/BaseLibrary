package com.hjh.baselib.file;

import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * ͼƬ
 * @author hjh
 * 2015-1-24����3:20:27
 *
 */
public final class ImageFile implements Serializable{
	
	    private static final long serialVersionUID = -6531455322049131559L;
	  
	  
	    private String imageId;
	    private String imageName;
	    private String imagePath;
	    private String thumbnailPath;
	    private boolean isSelected;
	    private Bitmap bitmap;
	    private String originalPath;//未组装前的原始path

	    public ImageFile() {

	    }

	    public ImageFile(String imageId, String imageName, String imagePath, String thumbnailPath, boolean isSelected, Bitmap bitmap) {
	        this.imageId = imageId;
	        this.imageName = imageName;
	        this.imagePath = imagePath;
	        this.thumbnailPath = thumbnailPath;
	        this.isSelected = isSelected;
	        this.bitmap = bitmap;
	    }

	    public String getImageId() {
	        return imageId;
	    }

	    public void setImageId(String imageId) {
	        this.imageId = imageId;
	    }

	    public String getImageName() {
	        return imageName;
	    }

	    public void setImageName(String imageName) {
	        this.imageName = imageName;
	    }

	    public String getImagePath() {
	        return imagePath;
	    }

	    public void setImagePath(String imagePath) {
	        this.imagePath = imagePath;
	    }

	    public String getThumbnailPath() {
	        return thumbnailPath;
	    }

	    public void setThumbnailPath(String thumbnailPath) {
	        this.thumbnailPath = thumbnailPath;
	    }

	    public boolean isSelected() {
	        return isSelected;
	    }

	    public void setIsSelected(boolean isSelected) {
	        this.isSelected = isSelected;
	    }

	    public Bitmap getBitmap(int width, int height) {
	        if (bitmap == null) {
	            try {
	                bitmap = FileScaner.getInstance().getImageThumbnail(imagePath, width, height);
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        return bitmap;
	    }

	    public void setBitmap(Bitmap bitmap) {
	        this.bitmap = bitmap;
	    }

	    @Override
	    public String toString() {
	        return "ImageBean{" +
	                "imageId='" + imageId + '\'' +
	                ", imagePath='" + imagePath + '\'' +
	                ", thumbnailPath='" + thumbnailPath + '\'' +
	                ", isSelected=" + isSelected +
	                ", bitmap=" + bitmap +
	                '}';
	    }

	    public String getOriginalPath() {
	        return originalPath;
	    }

	    public void setOriginalPath(String originalPath) {
	        this.originalPath = originalPath;
	    }
}
