package lib.utils;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {
	private static final char[] base64EncodeChars = new char[] { 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
			'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', '+', '/' };

	private static byte[] base64DecodeChars = new byte[] { -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59,
			60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1,
			-1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37,
			38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1,
			-1, -1 };

	private Base64Util() {
	}

	/**
	 * 将字节数组编码为字符串
	 * 
	 * @param data
	 */
	public static String encode(byte[] data) {
		StringBuffer sb = new StringBuffer();
		int len = data.length;
		int i = 0;
		int b1, b2, b3;

		while (i < len) {
			b1 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
				sb.append("==");
				break;
			}
			b2 = data[i++] & 0xff;
			if (i == len) {
				sb.append(base64EncodeChars[b1 >>> 2]);
				sb.append(base64EncodeChars[((b1 & 0x03) << 4)
						| ((b2 & 0xf0) >>> 4)]);
				sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
				sb.append("=");
				break;
			}
			b3 = data[i++] & 0xff;
			sb.append(base64EncodeChars[b1 >>> 2]);
			sb.append(base64EncodeChars[((b1 & 0x03) << 4)
					| ((b2 & 0xf0) >>> 4)]);
			sb.append(base64EncodeChars[((b2 & 0x0f) << 2)
					| ((b3 & 0xc0) >>> 6)]);
			sb.append(base64EncodeChars[b3 & 0x3f]);
		}
		return sb.toString();
	}

	/**
	 * 将base64字符串解码为字节数组
	 * 
	 * @param str
	 */
	public static byte[] decode(String str) {
		byte[] data = str.getBytes();
		int len = data.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
		int i = 0;
		int b1, b2, b3, b4;

		while (i < len) {

			/* b1 */
			if(i >= len)
				break;
			do {
				b1 = base64DecodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1) {
				break;
			}

			/* b2 */
			if(i >= len)
				break;
			do {
				b2 = base64DecodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1) {
				break;
			}
			buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));


			/* b3 */
			if(i >= len)
				break;
			do {
				b3 = data[i++];
				if (b3 == 61) {
					return buf.toByteArray();
				}
				b3 = base64DecodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1) {
				break;
			}
			buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));

			/* b4 */
			if(i >= len)
				break;
			do {
				b4 = data[i++];
				if (b4 == 61) {
					return buf.toByteArray();
				}
				b4 = base64DecodeChars[b4];
			} while (i < len && b4 == -1);
			if (b4 == -1) {
				break;
			}
			buf.write((int) (((b3 & 0x03) << 6) | b4));
		}
		buf.toString();
		return buf.toByteArray();
	}

	public static String MyEncoder(String str){
		byte[] data = str.getBytes();
		int len = data.length;
		/*switch (len % 3){
			case 0:
				break;
			case 1:
				str = str + "=";
				break;
			case 2:
				str = str + "==";
				break;
		}
		data = str.getBytes();
		len = data.length;*/
		StringBuffer stringBuffer = new StringBuffer();
		int i = 0;
		int b, b1, b2;
		while (i < len){
			b = data[i++];
			Log.e("ABC", "b: " + b);
			b2 = b >>> 4 & 0x0f;
			b1 = b & 0x1f;
			Log.e("ABC", "b1: " + b1 + "  b2: " + b2);
			stringBuffer.append(base64EncodeChars[b1 >= 41 ? b1 - 41 : b1]);
			stringBuffer.append(base64EncodeChars[b2 >= 41 ? b2 - 41 : b2]);
//			stringBuffer.append(b1);
//			stringBuffer.append(b2);
		}
		return stringBuffer.toString();
	}

	public static String myDecoder(String str){
		byte[] data = str.getBytes();
		StringBuffer stringBuffer = new StringBuffer();
		int i = 0;
		int len = data.length;
		int b1, b2, b3;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(len);
		while(i < len){
			b1 = data[i++];
			b2 = data[i++];
			b3 = (b1 & 0x0f ) | ((b2 & 0x0f) << 4);
			stringBuffer.append(base64DecodeChars[b3]);
//			buf.write(b3);
			Log.e("ABC", "b3: " + b3);
//			stringBuffer.append(b3);
		}
		/*try {
			Log.e("ABC", new String(buf.toByteArray(), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		Log.e("ABC", stringBuffer.toString());
		return stringBuffer.toString();
	}

	public static String DefaultEncoder(String s){
		s = "ai" + s + "1993";
		return new BASE64Encoder().encode(s.getBytes());
	}

	public static String DefaultDecoder(String s){
		try {
			String str = new String(new BASE64Decoder().decodeBuffer(s), "utf-8");
			return str.substring(2, str.length()-4);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
