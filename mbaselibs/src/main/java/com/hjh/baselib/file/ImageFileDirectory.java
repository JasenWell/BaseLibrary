package com.hjh.baselib.file;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ͼƬ�ļ���
 * @author hjh
 * 2015-1-24����3:22:02
 *
 */
public final class ImageFileDirectory implements Serializable{
	  private static final long serialVersionUID = 1080077087379957383L;

	  private String fileDirName;
	  private String fileDirPath;
	  private String bucketId;
	  private final List<ImageFile> files = new ArrayList<ImageFile>();
	  
	  public String getFileDirName(){
	    return this.fileDirName;
	  }
	  
	  public void setFileDirName(String fileDirName){
	    this.fileDirName = fileDirName;
	  }
	  
	  public String getFileDirPath(){
	    return this.fileDirPath;
	  }
	  
	  public void setFileDirPath(String fileDirPath){
	    this.fileDirPath = fileDirPath;
	  }
	  
	  public List<ImageFile> getFiles(){
	    return this.files;
	  }
	  
	  public void addImageFile(ImageFile file){
	    if (file != null) {
	      this.files.add(file);
	    }
	  }
	  
	  public String getBucketId() {
		return bucketId;
	  }

	public void setBucketId(String bucketId) {
		this.bucketId = bucketId;
	}

	public void clear(){
	    this.files.clear();
	  }
	  
	  public boolean equals(Object o){
	    ImageFileDirectory obj = (ImageFileDirectory)o;
	    
	    return (obj != null) && (this.fileDirName != null) && (obj.getFileDirName().equals(this.fileDirName));
	  }
	
}
