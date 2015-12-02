package lib.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import lib.MainApplication;

public class ActivityUtils {
	// private static final String ATTR_PACKAGE_STATS="PackageStats";
	private static Toast mToast;

	/*
	 * Get the height of the status bar
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;

		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());

			sbar = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			Log.d("", e.getMessage());
		}
		return sbar;
	}

	/**
	 * 设置第一个字符的颜色
	 * 
	 * @param str
	 * @return
	 */
	public final static SpannableStringBuilder setStartColor(String str, int color) {
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		style.setSpan(new ForegroundColorSpan(color), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;
	}

	public final static HashMap<String, String> getRealParams(HashMap<String, String> params) {
		HashMap<String, String> completelyParams = new HashMap<String, String>();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, String> entry = iterator.next();
			String value = entry.getValue();
			String key = entry.getKey();
			if (value != null) {
				if (!value.equals(""))
					completelyParams.put(key, value);
			}
		}

		return completelyParams;
	}

	/**
	 * 将目标转为字符串，null返回空字符串
	 * 
	 * @param tar
	 * @return
	 */
	public final static String nvl(Object tar) {
		if (tar == null)
			return "";
		return tar.toString();
	}

	public final static String houseNvl(Object tar, String string) {
		if (tar == null || tar.toString().equals(""))
			return string;
		return tar.toString();
	}

	public static String getLocalVersion() {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = MainApplication.getInstance().getPackageManager();
			PackageInfo pi = pm.getPackageInfo(MainApplication.getInstance().getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return null;
			}
		} catch (Exception e) {
			Log.d("", e.getMessage());
		}
		return versionName;
	}

	/**
	 * 检测横竖屏
	 */
	public final static void detectScreen(Activity source) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(source);
		boolean auto_switch = sp.getBoolean("auto_switch", true);
		// 禁止自动切换
		if (!auto_switch) {

			switch (source.getResources().getConfiguration().orientation) {
			case Configuration.ORIENTATION_PORTRAIT:
				// Do something here

				break;
			case Configuration.ORIENTATION_LANDSCAPE:
				// Do something here
				source.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				break;
			case Configuration.ORIENTATION_SQUARE:
				// Do something here
				source.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				break;

			}
			// 开启自动切换
		} else if (auto_switch) {
			source.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

			switch (source.getResources().getConfiguration().orientation) {
			case Configuration.ORIENTATION_PORTRAIT:
				// Do something here
				break;
			case Configuration.ORIENTATION_LANDSCAPE:
				// Do something here
				break;
			case Configuration.ORIENTATION_SQUARE:
				// Do something here
				break;

			}
		}
	}

	// 开关全屏
	public final static void detectFullScreen(Activity source) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(source);
		boolean fullscreen = sp.getBoolean("fullscreen", false);
		if (fullscreen) {
			source.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			source.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		} else {
			source.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			source.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

	public final static void setSpinnerData(Activity source, Spinner spinner, Object[] objArr) {
		ArrayAdapter<Object> spinnerCateAdapter = new ArrayAdapter<Object>(source,
				android.R.layout.simple_spinner_item);
		spinnerCateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for (Object obj : objArr) {
			spinnerCateAdapter.add(obj);
		}
		spinner.setAdapter(spinnerCateAdapter);
	}

	public final static void setSpinnerData(Activity source, Spinner spinner, Object objArr) {
		ArrayAdapter<Object> spinnerCateAdapter = new ArrayAdapter<Object>(source,
				android.R.layout.simple_spinner_item);
		spinnerCateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCateAdapter.add(objArr);
		spinner.setAdapter(spinnerCateAdapter);
	}



	/**
	 * Instead of Toast.make() method, the method must be executed in UI thread
	 * 
	 * @param msg
	 * @param timeLong
	 *            true means show message more time than false.
	 */
	public static void showTip(final String msg, final boolean timeLong) {
		//		Looper.prepare();
		try {
			if (MainApplication.currentActivity != null) {
				MainApplication.currentActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (mToast == null) {
							mToast = Toast.makeText(MainApplication.getInstance(), msg,
									timeLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
						} else {
							mToast.setText(msg);
						}
						mToast.show();
					}
				});
			}
		} catch (Exception e) {
			Log.d("", e.getMessage());
		}
	}

	public static void showTip(int msgResId) {
		showTip(MainApplication.getInstance().getString(msgResId), false);
	}

	public static void showTip(int msgResId, boolean timeLong) {
		showTip(MainApplication.getInstance().getString(msgResId), timeLong);
	}

	/**
	 * Same mAreaSelector showTip(), but the method is executed in UI thread
	 * 
	 * @param msg
	 * @param timeLong
	 */
	public static void showTipOnUIThread(String msg, boolean timeLong) {
		final boolean tLong = timeLong;
		final String str = msg;
		MainApplication.currentActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ActivityUtils.showTip(str, tLong);
			}
		});
	}

	public static void installAPK(String filePath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
		MainApplication.currentActivity.startActivity(i);
	}

//	public static int getTextApperanceID() {
//		return GlobalVariable.SCREEN_HEIGHT > 650 ? android.R.style.TextAppearance_Medium
//				: android.R.style.TextAppearance_Small;
//	}

	public static boolean isMobileNO(String mobiles) {
		if (mobiles.equals(""))
			return true;
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[6,7,8]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static String iDCardValidate(String IDStr) throws ParseException {
		String errorInfo = "";// 记录错误信息
		String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5",
				"8", "4", "2" };
		String Ai = "";
		if (IDStr.equals("")) {
			errorInfo = "身份证号码不得为空";
			return errorInfo;
		}
		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 15 && IDStr.length() != 18) {
			errorInfo = "身份证号码长度应该为15位或18位。";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		} else if (IDStr.length() == 15) {
			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
		}
		if (isNumeric(Ai) == false) {
			errorInfo = "身份证15位号码都应为数字 ; 18位号码除最后一位外，都应为数字。";
			return errorInfo;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInfo = "身份证生日无效。";
			return errorInfo;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay)
							.getTime()) < 0) {
				errorInfo = "身份证生日不在有效范围。";
				return errorInfo;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInfo = "身份证月份无效";
			return errorInfo;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInfo = "身份证日期无效";
			return errorInfo;
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Hashtable<String, String> h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			errorInfo = "身份证地区编码错误。";
			return errorInfo;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi + Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				errorInfo = "身份证无效，不是合法的身份证号码";
				return errorInfo;
			}
		} else {
			return "";
		}
		// =====================(end)=====================
		return "";
	}

	/**
	 * 功能：设置地区编码
	 *
	 * @return Hashtable 对象
	 */
	private static Hashtable<String, String> GetAreaCode() {
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}

	/**
	 * 功能：判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	private static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能：判断字符串是否为日期格式
	 * 
	 * @param strDate
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}


	public static void openCamera(Activity c, Intent i, int requestCode) {
		String path = i.getStringExtra("path");
		if (path != null) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
			c.startActivityForResult(intent, requestCode);
		}
	}

	/*Android 5.0一出来后，其中有个特性就是Service Intent  must be explitict，
	也就是说从Lollipop开始，service服务必须采用显示方式启动*/
	public static Intent getServiceIntent(Context context, String action){
		if(Build.VERSION.SDK_INT >= 21){
			return new Intent(
					getExplicitIntent(context, new Intent(action)));
		}else {
			return new Intent(action);
		}
	}

	/*隐式启动转换为显示启动，官方推荐*/
	private static Intent getExplicitIntent(Context context, Intent implicitIntent) {
		// Retrieve all services that can match the given intent
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
		// Make sure only one match was found
		if (resolveInfo == null || resolveInfo.size() != 1) {
			return null;
		}
		// Get component info and create ComponentName
		ResolveInfo serviceInfo = resolveInfo.get(0);
		String packageName = serviceInfo.serviceInfo.packageName;
		String className = serviceInfo.serviceInfo.name;
		ComponentName component = new ComponentName(packageName, className);
		// Create a new intent. Use the old one for extras and such reuse
		Intent explicitIntent = new Intent(implicitIntent);
		// Set the component to be explicit
		explicitIntent.setComponent(component);
		return explicitIntent;
	}


}
