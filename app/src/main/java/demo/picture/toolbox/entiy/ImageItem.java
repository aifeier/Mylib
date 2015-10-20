package demo.picture.toolbox.entiy;

import java.io.IOException;
import java.io.Serializable;

import android.graphics.Bitmap;

import demo.picture.toolbox.BitmapTemp;


public class ImageItem implements Serializable {

	/**
	 * 文件夹的照片列表
	 */
	private static final long serialVersionUID = 1L;
	/*图片Id*/
	public String imageId;
	/*缩略图路径*/
	public String thumbnailPath;
	/*图片完整路径*/
	public String imagePath;
	/*是否被选中*/
	public boolean isSelected = false;

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	/*如果有缩略图则返回缩略图路径，否则返回完整路径*/
	public String getThumbnailPath() {
		if(thumbnailPath != null )
			return thumbnailPath;
		else
			return imagePath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

}
