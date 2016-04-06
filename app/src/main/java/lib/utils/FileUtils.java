package lib.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
	
	public static String ObbDir = null;
	public static String logCache = ObbDir + "/logs";
	public static String photoCache = ObbDir + "/photos";
	public static String fileCache = ObbDir + "/files";
	public static String OtherCache = ObbDir + "/cache";
	private static FileUtils instance;


	public static FileUtils getInstance(Context context){
		if(instance == null) {
			ObbDir = context.getExternalCacheDir().getParentFile().getAbsolutePath();
			File file = new File(ObbDir);
			if (!file.exists()) {
				file.mkdirs();
			}
			logCache = ObbDir + "/logs";
			file = new File(logCache);
			if (!file.exists()) {
				file.mkdirs();
			}
			photoCache = ObbDir + "/photos";
			file = new File(photoCache);
			if (!file.exists()) {
				file.mkdirs();
			}
			fileCache = ObbDir + "/files";
			file = new File(fileCache);
			if (!file.exists()) {
				file.mkdirs();
			}
			OtherCache = ObbDir + "/files";
			file = new File(OtherCache);
			if (!file.exists()) {
				file.mkdirs();
			}
			instance = new FileUtils();
		}
		return instance;
	}


	public static void saveBitmap(Bitmap bm, String picName) {
		try {
			File f = new File(photoCache, picName + ".jpg");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 50, out);
			out.flush();
			out.close();
			bm.recycle();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}




	public static void delFile(String fileName){
		File file = new File(fileName);
		if(file.isFile()){
			file.delete();
        }
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(ObbDir);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;

		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete();
			else if (file.isDirectory())
				deleteDir();
		}
		dir.delete();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

	/*保存本地文件，copy本地文件，saveName包含后缀*/
	public static void saveFile(String filePath, String saveName){
		File inFile = new File(filePath);
		File outFile = new File(fileCache, saveName);
		if(!inFile.exists())
			return;
		try {
			FileInputStream inputStream = new FileInputStream(inFile);
			FileOutputStream outputStream = new FileOutputStream(outFile, true);
			byte[] buf = new byte[2048];
			int len = 0;
			while ((len = inputStream.read(buf)) != -1){
				outputStream.write(buf, 0, len);
			}
			outputStream.close();
			outputStream.flush();
			inputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

}
