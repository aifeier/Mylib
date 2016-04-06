package com.cwf.app.cwf;

/**
 * @author liujingxing  on 16/1/12.
 */
public class GlobalCrashHandler extends CrashHandler {

    private static GlobalCrashHandler mInstance = new GlobalCrashHandler();

    private GlobalCrashHandler() {
    }

    public static GlobalCrashHandler getInstance() {
        return mInstance;
    }

    @Override
    public void upLoadErrorFile(String filePath) {
//        //程序崩溃后页面会一直卡着,如果按默认的超时时间(30s),请求无响应时，页面会卡住30s,所以要重新设置超时时间
//        HttpSender.setConnectTimeout(3);
//        HttpSender.setReadTimeout(3);
//        HttpSender.setWriteTimeout(3);
//        ErrorLogUtil errorLogUtil = new ErrorLogUtil(mContext);
//        errorLogUtil.upLoadErrorFile(filePath, errorTitle);
//        HttpSender.setDefaultTimeout();//错误日志不管上传成功与否都还原默认的超时时间
    }
}
