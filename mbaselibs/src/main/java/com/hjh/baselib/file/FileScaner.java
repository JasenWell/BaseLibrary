package com.hjh.baselib.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.SparseArray;

/**
 * �����ļ�ɨ��
 * @author hjh
 * 2015-1-24����3:18:51
 *
 */
public final class FileScaner {
	
	private static FileScaner instance;
	private SparseArray<String> thumbnails = new SparseArray<String>();
	private Map<String, ImageFileDirectory> dirList = new HashMap<String,ImageFileDirectory>();
    private boolean hasBuildImagesBucketList = false;
	
	public static FileScaner getInstance(){
		if(instance == null){
			syncInit();
		}
		return instance;
	}
	
	private synchronized static void syncInit(){
		if(instance == null){
			instance = new FileScaner();
		}
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	@Deprecated
	public List<ImageFileDirectory> scanImageFile(Context context){
	    if (context == null) {
	      return null;
	    }
	    
	    Map<String, ImageFileDirectory> map = new HashMap<String, ImageFileDirectory>();
	    List<ImageFileDirectory> list = new ArrayList<ImageFileDirectory>();
	    List<ImageFileDirectory> temp = new ArrayList<ImageFileDirectory>();
	    
	    Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	    ContentResolver mContentResolver = context.getContentResolver();
	    Cursor mCursor = null;
	    String name;
	    try{
	      mCursor = mContentResolver.query(mImageUri, new String[] { "_data", "_display_name" }, 
	        "mime_type= ? or mime_type=?", 
	        
	        new String[] { "image/jpeg", "image/png" }, "date_modified DESC");
	      while (mCursor.moveToNext()){
	        String path = mCursor.getString(mCursor.getColumnIndex("_data"));
	        name = mCursor.getString(mCursor.getColumnIndex("_display_name"));
	        
	        File parent = new File(path);
	        
	        String parentName = parent.getParentFile().getName();
	        String parentPath = parent.getParentFile().getAbsolutePath();
	        
	        ImageFile file = new ImageFile();
	        file.setImageName(name);
	        file.setImagePath(path);
	        if (!map.containsKey(parentName)){
	          ImageFileDirectory dir = new ImageFileDirectory();
	          dir.setFileDirName(parentName);
	          dir.setFileDirPath(parentPath);
	          dir.addImageFile(file);
	          list.add(dir);
	          map.put(parentName, dir);
	        }else{
	          ((ImageFileDirectory)map.get(parentName)).addImageFile(file);
	        }
	      }
	    }catch (Exception e){
	      e.printStackTrace();
	    }finally{
	      if (mCursor != null) {
	        mCursor.close();
	      }
	    }
	    
	    for (ImageFileDirectory dir : list) {
	      if ((dir.getFiles() != null) && (dir.getFiles().size() > 0)) {
	        temp.add(dir);
	      }
	    }
	    
	    list.clear();
	    
	    return temp;
	  }
	
	//扫描视频文件
	public  List<VideoFile> scanVideoFiles(Context context){
		 List<VideoFile> list = null;  
	        if (context != null) {  
	            Cursor cursor = context.getContentResolver().query(  
	                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null,null, null);  
	            if (cursor != null) {  
	                list = new ArrayList<VideoFile>();  
	                while (cursor.moveToNext()) {  
	                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));  
	                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));  
	                    String album = cursor.getString(cursor  
	                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ALBUM));  
	                    String artist = cursor.getString(cursor  
	                                    .getColumnIndexOrThrow(MediaStore.Video.Media.ARTIST));  
	                    String displayName = cursor.getString(cursor  
	                                    .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));  
	                    String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));  
	                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));  
	                    long duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));  
	                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));  
	                    VideoFile video = new VideoFile(id, title, album, artist, displayName, mimeType, path, size, duration);  
	                    list.add(video);  
	                }  
	                cursor.close();  
	            }  
	        }  
	        
	        return list;  
	}
	
	/**
	 * 获取所有视频
	 * @param list 填充List
	 * @param file
	 */
	public void scanVideoFiles(final List<VideoFile> list, File file){
		file.listFiles(new FileFilter() {  
			  
            @Override  
            public boolean accept(File file) {  
                // sdCard找到视频名称   
                String name = file.getName();  
  
                int i = name.indexOf('.');  
                if (i != -1) {  
                    name = name.substring(i);  
                    if (name.equalsIgnoreCase(".mp4")  
                            || name.equalsIgnoreCase(".3gp")  
                            || name.equalsIgnoreCase(".wmv")  
                            || name.equalsIgnoreCase(".ts")  
                            || name.equalsIgnoreCase(".rmvb")  
                            || name.equalsIgnoreCase(".mov")  
                            || name.equalsIgnoreCase(".m4v")  
                            || name.equalsIgnoreCase(".avi")  
                            || name.equalsIgnoreCase(".m3u8")  
                            || name.equalsIgnoreCase(".3gpp")  
                            || name.equalsIgnoreCase(".3gpp2")  
                            || name.equalsIgnoreCase(".mkv")  
                            || name.equalsIgnoreCase(".flv")  
                            || name.equalsIgnoreCase(".divx")  
                            || name.equalsIgnoreCase(".f4v")  
                            || name.equalsIgnoreCase(".rm")  
                            || name.equalsIgnoreCase(".asf")  
                            || name.equalsIgnoreCase(".ram")  
                            || name.equalsIgnoreCase(".mpg")  
                            || name.equalsIgnoreCase(".v8")  
                            || name.equalsIgnoreCase(".swf")  
                            || name.equalsIgnoreCase(".m2v")  
                            || name.equalsIgnoreCase(".asx")  
                            || name.equalsIgnoreCase(".ra")  
                            || name.equalsIgnoreCase(".ndivx")  
                            || name.equalsIgnoreCase(".xvid")) {  
                        VideoFile vi = new VideoFile();  
                        vi.setDisplayName(file.getName());  
                        vi.setPath(file.getAbsolutePath());
                        list.add(vi);  
                        return true;  
                    }  
                } else if (file.isDirectory()) {  
                	scanVideoFiles(list, file);  
                }  
                return false;  
            }  
        });  

		
	}
	
	 /** 图像缩略图
     * 根据指定的图像路径和大小来获取缩略图 
     * 此方法有两点好处： 
     *     1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度， 
     *        第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 
     *     2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 
     *        用这个工具生成的图像不会被拉伸。 
     * @param imagePath 图像的路径 
     * @param width 指定输出图像的宽度 
     * @param height 指定输出图像的高度 
     * @return 生成的缩略图 
     */  
    public Bitmap getImageThumbnail(String imagePath, int width, int height) {  
        Bitmap bitmap = null;  
        BitmapFactory.Options options = new BitmapFactory.Options();  
        options.inJustDecodeBounds = true;  
        // 获取这个图片的宽和高，注意此处的bitmap为null   
        bitmap = BitmapFactory.decodeFile(imagePath, options);  
        options.inJustDecodeBounds = false; // 设为 false   
        // 计算缩放比   
        int h = options.outHeight;  
        int w = options.outWidth;  
        int beWidth = w / width;  
        int beHeight = h / height;  
        int be = 1;  
        if (beWidth < beHeight) {  
            be = beWidth;  
        } else {  
            be = beHeight;  
        }  
        if (be <= 0) {  
            be = 1;  
        }  
        options.inSampleSize = be;  
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false   
        bitmap = BitmapFactory.decodeFile(imagePath, options);  
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象   
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
        return bitmap;  
    }  

	
	/**
	 * 生成视频缩略图
	 * @param videoPath
	 * @param width
	 * @param height 输出的高
	 * @param kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。 
     *            其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96 
	 * @return
	 */
	 public Bitmap getVideoThumbnail(String videoPath, int width, int height,int kind) {  
	        Bitmap bitmap = null;  
	        // 获取视频的缩略图   
	        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
	        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,  
	                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
	        return bitmap;  
     }  
	 
	 
	 private void getThumbnail(Context context) {
	        String[] projection = {
	                MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA
	        };
	        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection,
	                null, null, null);
	        getThumbnailColumnData(cursor);
     }

	    private void getThumbnailColumnData(Cursor cursor) {
	        if (cursor.moveToFirst()) {
	            int _id;
	            int img_id;
	            String thumb_path;
	            int _idColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails._ID);
	            int img_idColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
	            int dataColumn = cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
	            do {
	                _id = cursor.getInt(_idColumn);
	                img_id = cursor.getInt(img_idColumn);
	                String thumb_path1 = cursor.getString(dataColumn);
//	                if (thumb_path1.contains("/storage/sdcard0/"))
//	                    thumb_path = thumb_path1.replace("/storage/sdcard0/", "file:///mnt/sdcard/");
//	                else {
	                    thumb_path = "file://" + thumb_path1;
//	                }
	                thumbnails.put(img_id, thumb_path);
	            } while (cursor.moveToNext());
	        }
	        cursor.close();
	    }
	    
	    public void buildImagesBucketList(Context context) {
	        getThumbnail(context);
	        String[] columns = {
	                MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID,
	                MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME,
	                MediaStore.Images.Media.TITLE, MediaStore.Images.Media.SIZE,
	                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
	        };
	        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
	                columns, null, null, "date_modified DESC");
	        if (cursor.moveToFirst()) {
	            int photoIDIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
	            int bucketIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
	            int photoPathIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	            int photoNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
	            int photoTitleIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
	            int photoSizeIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
	            int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

	            do {
	                String _id = cursor.getString(photoIDIndex);
	                String name = cursor.getString(photoNameIndex);
	                String path1 = cursor.getString(photoPathIndex);

	                //为使ImageLoader正确加载图片
	                String path;
//	                if (path1.contains("/storage/sdcard0/"))
//	                    path = path1.replace("/storage/sdcard0/", "file:///mnt/sdcard/");
//	                else {
	                    path = "file://" + path1;
//	                }
	                String title = cursor.getString(photoTitleIndex);
	                String size = cursor.getString(photoSizeIndex);
	                String bucketName = cursor.getString(bucketDisplayNameIndex);
	                String bucketId = cursor.getString(bucketIdIndex);

	                ImageFileDirectory bucket = dirList.get(bucketId);
	                if (bucket == null) {
	                    bucket = new ImageFileDirectory();
	                    dirList.put(bucketId, bucket);
	                    bucket.setFileDirName(bucketName);
	                    File parent = new File(path1);
	        	        String parentPath = parent.getParentFile().getAbsolutePath();
	                    bucket.setFileDirPath(parentPath);
	                    bucket.setBucketId(bucketId);
	                }
	                
	                ImageFile imageItem = new ImageFile();
	                imageItem.setImageId(_id);
	                imageItem.setImageName(name);
	                imageItem.setImagePath(path);
	                imageItem.setOriginalPath(path1);
	                imageItem.setThumbnailPath(thumbnails.get(Integer.parseInt(_id)));
	                bucket.addImageFile(imageItem);

	            } while (cursor.moveToNext());
	        }
	        cursor.close();

	        hasBuildImagesBucketList = true;
	    }

	    /**
	     * 
	     * @param context
	     * @param refresh 是否需要刷新重新获取
	     * @return
	     */
	    public List<ImageFileDirectory> scanImageFile(Context context,boolean refresh) {
	        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
	            dirList.clear();
	            buildImagesBucketList(context);
	        }
	        List<ImageFileDirectory> tmpList = new ArrayList<ImageFileDirectory>();
	        Iterator<Map.Entry<String, ImageFileDirectory>> itr = dirList.entrySet().iterator();
	        while (itr.hasNext()) {
	            Map.Entry<String, ImageFileDirectory> entry = itr.next();
	            tmpList.add(entry.getValue());
	        }
	        return tmpList;
	    }


	    /**
	     * 扫描媒体库
	     * 将扫描到的照片添加到媒体提供者的数据库，实时更新
	     */
	    public void scannerMedia(Context context,String filePath) {
	        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	        File f = new File(filePath);
	        Uri contentUri = Uri.fromFile(f);
	        mediaScanIntent.setData(contentUri);
	        context.sendBroadcast(mediaScanIntent);
	    }
	    
	   
}
