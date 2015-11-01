package lib.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
	static SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static SimpleDateFormat getSimpleDateFormat(){
		return simpleDateFormat;
	}
	
	
	
	
	public static SimpleDateFormat getSimpleDateFormat(String format){
		return new SimpleDateFormat(format);
	}
	
	/**
	 * 获得当前时间  按照格式：formatString
	 * @return
	 */
	public static String getSimpleDate(String formatString){
		return new SimpleDateFormat(formatString).format(new Date());
	}
	
	/**
	 * 获得当前时间  按照格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getSimpleDate(){
		return getSimpleDateFormat().format(new Date());
	}
	
	/**
	 * 获得当前时间  按照格式：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getSimpleDate(Date date){
		return getSimpleDateFormat().format(date);
	}

	/**
	 * 获得2个时间的间隔秒数
	 * @param old_date 旧时间
	 * @param new_date 新时间
	 * @return
	 */
	public static long getIntervalTime(Date old_date, Date new_date) {
		long second = 0;
		try {
			// 得到间隔秒数
			second = (new_date.getTime() - old_date.getTime());
		} catch (Exception e) {
			return 0;
		}
		return second;
	}
	
	/**
	 * 比较时间大小、先后，
	 * time1比time2晚返回1
	 * time1比time2早返回-1
	 * 一样返回0
	 * @param time1
	 * @param time2
	 * @return
	 * @throws ParseException
	 */
	public static int dateTimeCompara(String time1, String time2)
			throws ParseException {
		Date dt1 = simpleDateFormat.parse(time1);
		Date dt2 = simpleDateFormat.parse(time2);
		if (dt1.getTime() > dt2.getTime()) {
//			System.out.println("dt1 在dt2前");
			return 1;
		} else if (dt1.getTime() < dt2.getTime()) {
//			System.out.println("dt1在dt2后");
			return -1;
		} else {
			return 0;
		}
	}

	/*计算视频的时长，返回hh:mm:ss格式的时间*/
	public static String intToString(int time){
		String str = "";
		if(time > 0){
			int hours = time / 3600;
			int minutes = (time - hours * 3600) / 60;
			int second = time % 60;
			if(hours >0)
				str = hours + ":";
			if(minutes >0 || hours>0) {
				if (minutes < 10) {
					if(minutes == 0)
						str = str + "00:";
					else
						str = str + "0" + minutes + ":";
				}
				else
					str = str + hours + ":";
			}else
				str = "00:";
			if(second > 0 || minutes> 0){
				if(minutes > 0 && second < 10){
					if(second == 0)
						str = str + "00";
					else
						str = str + "0" + second;
				}
				else
					str = str + second;
			}

		}

		return str;
	}
}
