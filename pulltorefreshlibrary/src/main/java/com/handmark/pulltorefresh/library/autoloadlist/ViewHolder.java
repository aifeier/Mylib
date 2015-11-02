package com.handmark.pulltorefresh.library.autoloadlist;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;


public class ViewHolder {

	private Context mContext;
	private SparseArray<View> mViews;//存储item中的子view
	private View mConvertView; //item的view
	public int mPosition;//当前item的位置
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
	private Handler handler = new Handler();

	public ViewHolder() {
	}

	public ViewHolder(Context context, int position, ViewGroup parent, int itemLayoutId) {
		mContext = context;
		mPosition = position;
		mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(itemLayoutId, parent, false);
		mConvertView.setTag(this);

	}

	/**
	 * 实例化viewholder
	 * @param context 上下文
	 * @param position item位置
	 * @param convertView  item的view
	 * @param parent  item的父雷
	 * @param itemLayoutId  item的layoutId
	 * @return  返回viewholder对象
	 */
	public static ViewHolder instances(Context context, int position, View convertView,
			ViewGroup parent, int itemLayoutId) {
		if (convertView == null) {
			return new ViewHolder(context, position, parent, itemLayoutId);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}

	}

	/**
	 * 根据viewId获取item中对应的子view
	 * @param viewId  item中子view的Id
	 * @return  返回item中的子view
	 */
	public View findViewById(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = getConvertView().findViewById(viewId);
			mViews.put(viewId, view);
		}
		return view;

	}

	/**
	 * 得到每个item的view
	 * @return view
	 */
	public View getConvertView() {
		return mConvertView;
	}

	public void setTag(int viewId, String tag) {
		findViewById(viewId).setTag(tag);
	}

	/**
	 * 给TextView设置值
	 * @param viewId  textView的id
	 * @param value  要设置的值
	 * @return  返回viewholder对象，便于链式编程
	 */
	public ViewHolder setValueToTextView(int viewId, String value) {
		TextView tv = (TextView) findViewById(viewId);
		tv.setText(value);
		return this;
	}

	/**
	 * 给button设置值
	 * @param viewId button的id
	 * @param value 要设置的值
	 * @return 返回viewholder对象，便于链式编程
	 */
	public ViewHolder setValueToButton(int viewId, String value) {
		Button btn = (Button) findViewById(viewId);
		btn.setText(value);
		return this;
	}

	public ViewHolder setUrlToImageView(int viewId, String url, int errImg, int loadingImg){
		Glide.with(mContext)
				.load(url)
				.error(errImg)
				.placeholder(loadingImg)
				.into((ImageView) findViewById(viewId));
		return this;
	}
	/*设置视频到TextureView中
	* TextureView OR SurfaceView*/
	public ViewHolder setVideoToView(int viewId, String video_uri){
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			initTextureView(viewId, video_uri);
		}
		return this;
	}

	private boolean isPreperad = false;
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void initTextureView(int viewId,  final String video_uri){
		final MediaPlayer mediaPlayer = new MediaPlayer();
		TextureView textureView = (TextureView) findViewById(viewId);
		textureView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 320));
		textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
			@Override
			public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
				try {
					mediaPlayer.setSurface(new Surface(surface));
					mediaPlayer.setDataSource(mContext, Uri.parse(video_uri));
					mediaPlayer.prepareAsync();
					mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						@Override
						public void onPrepared(MediaPlayer mp) {
							isPreperad = true;
						}
					});
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

			}

			@Override
			public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
				mediaPlayer.release();
				return true;
			}

			@Override
			public void onSurfaceTextureUpdated(SurfaceTexture surface) {

			}
		});
		textureView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mediaPlayer!=null && isPreperad){
					if(mediaPlayer.isPlaying())
						mediaPlayer.pause();
					else
						mediaPlayer.start();
				}
			}
		});
	}


	/*设置图片列表到GridView中*/
	public ViewHolder setPicturesToGridView(int viewId,final ArrayList<String> strUrl, final int errImg, final int loadingImg){
		GridView gridView = (GridView) findViewById(viewId);
		AutoLoadAdapter<String> autoLoadAdapter = new AutoLoadAdapter<String>(mContext, R.layout.item_photo) {
			@Override
			public void buildView(ViewHolder holder, String data) {
				holder.setUrlToImageView(R.id.item_imageview, data, errImg, loadingImg);
			}

			@Override
			public void getPage(int page) {

			}
		};
		autoLoadAdapter.setmData(strUrl, null);
		if(strUrl.size()==1){
			gridView.setNumColumns(1);
		}else{
			gridView.setNumColumns(3);
		}
		gridView.setAdapter(autoLoadAdapter);
		return this;
	}

	public ViewHolder setUrlToImageView(int viewId, int img, int errImg, int loadingImg){
		Glide.with(mContext)
				.load(img)
				.error(errImg)
				.placeholder(loadingImg)
				.into((ImageView) findViewById(viewId));
		return this;
	}

	/**
	 * 给ImageView设置bitmap
	 * @param viewId ImageView的id
	 * @param bitmap 要设置的bitmap
	 * @return 返回viewholder对象，便于链式编程
	 */
	public ViewHolder setImageBitmapToImageView(int viewId, Bitmap bitmap) {
		return setImageBitmapToImageViewAndSetTag(viewId, bitmap, mPosition + "");
	}


	/**
	 * 给ImageView设置bitmap
	 * @param viewId ImageView的id
	 * @param bitmap 要设置的bitmap
	 * @param tag 要设置的tag
	 * @return 返回viewholder对象，便于链式编程
	 */
	public ViewHolder setImageBitmapToImageViewAndSetTag(int viewId, Bitmap bitmap, String tag) {
		ImageView imageView = (ImageView) findViewById(viewId);
		imageView.setImageBitmap(bitmap);
		imageView.setTag(tag);
		return this;
	}

	/*
	重新计算缩略图尺寸
	* */
	public Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 256)
					&& (options.outHeight >> i <= 256)) {
				in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
}
