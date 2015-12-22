package com.example.idcard_demo;
/**
 * 
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.cwf.app.alllibrary.R;


/**
 *
 * 项目名称：PassportReader_Sample_Sdk 类名称：ShowResultActivity 类描述： 创建人：yujin
 * 创建时间：2015-6-12 上午10:25:47 修改人：yujin 修改时间：2015-6-12 上午10:25:47 修改备注：
 *
 * @version
 * 
 */
public class ShowResultActivity extends Activity implements OnClickListener {
	private DisplayMetrics displayMetrics = new DisplayMetrics();
	private int srcWidth, srcHeight;
	private EditText et_recogPicture;
	private String recogResult = "";
	private String exception;
	private String[] splite_Result;
	private String result = "";
	private Button btn_repeat_takePic, btn_back;
	private String devcode = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		srcWidth = displayMetrics.widthPixels;
		srcHeight = displayMetrics.heightPixels;
		setContentView(R.layout.activity_show_result);
		Intent intent = getIntent();
		recogResult = intent.getStringExtra("recogResult");
		exception = intent.getStringExtra("exception");
		devcode = intent.getStringExtra("devcode");
		findView();
	}

	/**
	 * @Title: findView
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void findView() {
		// TODO Auto-generated method stub
		et_recogPicture = (EditText) this.findViewById(R.id.et_recogPicture);
		btn_repeat_takePic = (Button) this.findViewById(R.id.btn_repeat_takePic);
		btn_repeat_takePic.setOnClickListener(this);
		btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(srcWidth,
				(int) (srcHeight * 0.9));
		et_recogPicture.setLayoutParams(params);
		params = new RelativeLayout.LayoutParams((int) (srcWidth * 0.15),
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.BELOW, R.id.et_recogPicture);
		btn_back.setLayoutParams(params);
		params = new RelativeLayout.LayoutParams((int) (srcWidth * 0.15),
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		params.addRule(RelativeLayout.BELOW, R.id.et_recogPicture);
		btn_repeat_takePic.setLayoutParams(params);
		if (exception != null && !exception.equals("")) {
			et_recogPicture.setText(exception);
		} else {
			splite_Result = recogResult.split(",");
			for (int i = 0; i < splite_Result.length; i++) {
				if (result.equals("")) {
					result = splite_Result[i] + "\n";
				} else {
					result = result + splite_Result[i] + "\n";
				}

			}
			et_recogPicture.setText(result);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.btn_repeat_takePic) {
			Intent intent = new Intent(ShowResultActivity.this, CameraActivity.class);
			intent.putExtra("nMainId",
					SharedPreferencesHelper.getInt(getApplicationContext(), "nMainId", 2));
			intent.putExtra("devcode", devcode);
			ShowResultActivity.this.finish();
			startActivity(intent);
		}
		else if(v.getId() ==R.id.btn_back) {
			finish();
		}


	}
}
