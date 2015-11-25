package lib.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.cwf.app.cwf.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by n-240 on 2015/11/24.
 */
public class FileExploreActivity extends Activity implements AdapterView.OnItemClickListener{

    public static final String FILEDIALOG_PATH = "path";
    public static final String FILEDIALOG_NAME = "name";
    public static final String FILEDIALOG_IMG = "img";

    public static final String KEY_ROOT_PATH = "root_path";

    public static final String sRoot = "/";
    public static final String sParent = "..";
    public static final String sFolder = ".";

    private ListView listView;
    private List<Map<String, Object>> listData = null;
    private SimpleAdapter adapter;
    private Map<String, Integer> images;/*不同文件显示的图片*/
    private String root;/*显示的文件根目录*/
    private String path;/*显示的文件当前目录*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_base_list);
        setTitle("文件管理器");
        initData();
        initView();
    }

    private void initView(){
        listView = (ListView) findViewById(R.id.base_list);
        refreshListData();
        listView.setOnItemClickListener(this);
    }

    private void refreshListData(){
        // 刷新文件列表
        File[] files = null;
        try {
            files = new File(path).listFiles();
        } catch (Exception e) {
            files = null;
        }
        if (files == null) {
            // 访问出错
            ActivityUtils.showTip("访问出错", false);
        }
        if(listData!=null){
            listData.clear();
        }else{
            listData = new ArrayList<>(files.length);
        }

        // for saving the file and folder
        ArrayList<Map<String, Object>> lfolders = new ArrayList<Map<String, Object>>();
        ArrayList<Map<String, Object>> lfiles = new ArrayList<Map<String, Object>>();

        if (!this.path.equals(root)) {
            // add root folder and previous folder
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(FILEDIALOG_NAME, sRoot);
            map.put(FILEDIALOG_PATH, root);
            map.put(FILEDIALOG_IMG, getImageId(sRoot));
            listData.add(map);

            String pathParent = path.substring(0, path.lastIndexOf("/"));
            map = new HashMap<String, Object>();
            map.put(FILEDIALOG_NAME, sParent);
            map.put(FILEDIALOG_PATH, pathParent);
            map.put(FILEDIALOG_IMG, getImageId(sParent));
            listData.add(map);
        }

        for (File file : files) {
            if (file.isDirectory() && file.listFiles() != null) {
                // add folder
                Map<String, Object> map = new HashMap<String, Object>();
                map.put(FILEDIALOG_NAME, file.getName());
                map.put(FILEDIALOG_PATH, file.getPath());
                map.put(FILEDIALOG_IMG, getImageId(sFolder));
                lfolders.add(map);
            } else if (file.isFile()) {
                // add file
                String sf = getSuffix(file.getName()).toLowerCase();
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put(FILEDIALOG_NAME, file.getName());
                    map.put(FILEDIALOG_PATH, file.getPath());
                    map.put(FILEDIALOG_IMG, getImageId(sf));
                    lfiles.add(map);
            }
        }

        listData.addAll(lfolders);
        listData.addAll(lfiles);

        SimpleAdapter adapter = new SimpleAdapter(
                this,
                listData,
                R.layout.filedialogitem,
                new String[] { FILEDIALOG_IMG, FILEDIALOG_NAME, FILEDIALOG_PATH },
                new int[] { R.id.filedialogitem_img,
                        R.id.filedialogitem_name, R.id.filedialogitem_path });
        listView.setAdapter(adapter);

    }

    private String getSuffix(String filename) {
        int dix = filename.lastIndexOf('.');
        if (dix < 0) {
            return "";
        } else {
            return filename.substring(dix + 1);
        }
    }

    /*获得图片ID*/
    private int getImageId(String s) {
        if (images == null) {
            return 0;
        } else if (images.containsKey(s)) {
            return images.get(s);
        } else if (images.containsKey("")) {
            return images.get("");
        } else {
            return 0;
        }
    }

    private void initData(){
        if(getIntent().hasExtra(KEY_ROOT_PATH))
            root = getIntent().getStringExtra(KEY_ROOT_PATH);
        if(root == null || root.trim().length() == 0)
            root = Environment.getExternalStorageDirectory().getPath();
        path = root;

        images = new HashMap<String, Integer>();
/*        images.put(sRoot, R.drawable.filedialog_root);
        images.put(sParent, R.drawable.filedialog_parent);
        images.put(sFolder, R.drawable.filedialog_folder);
        images.put("bmp", R.drawable.filedialog_bmpfile);
        images.put("jpg", R.drawable.filedialog_jpgfile);
        images.put("png", R.drawable.filedialog_pngfile);
        images.put("doc", R.drawable.filedialog_docfile);
        images.put("docx", R.drawable.filedialog_docfile);
        images.put("ppt", R.drawable.filedialog_pptfile);
        images.put("pptx", R.drawable.filedialog_pptfile);
        images.put("xls", R.drawable.filedialog_xlsfile);
        images.put("xlsx", R.drawable.filedialog_xlsfile);
        images.put("txt", R.drawable.filedialog_txtfile);
        images.put("wps", R.drawable.filedialog_docfile);
        images.put("log", R.drawable.filedialog_txtfile);*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // select item
        String pt = (String) listData.get(position).get(FILEDIALOG_PATH);
        String fn = (String) listData.get(position).get(FILEDIALOG_NAME);
        if (fn.equals(root)){
            // return root folder
            path = root;
        } else if(fn.equals(sParent)) {
            // return previous folder
            if(!pt.contains(root))
                path = root;
            else
                path = pt;
        } else {
            File fl = new File(pt);
            if (fl.isFile()) {
                openFile(fl);
                return;
            } else if (fl.isDirectory()) {
                // if it is a folder, so enters the folder
                if(!pt.contains(root))
                    path = root;
                else
                    path = pt;
            }
        }
        refreshListData();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            if(path == root){
                finish();
            }else{
                path = path.substring(0, path.lastIndexOf("/"));
                if(!path.contains(root))
                    path = root;
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 打开文件
     * @param file
     */
    private void openFile(File file){

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        startActivity(intent);

    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     * @param file
     */
    private String getMIMEType(File file) {

        String type="*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
    /* 获取文件的后缀名 */
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="")return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){ //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if(end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private final String[][] MIME_MapTable={
            //{后缀名， MIME类型}
            {".3gp",    "video/3gpp"},
            {".apk",    "application/vnd.android.package-archive"},
            {".asf",    "video/x-ms-asf"},
            {".avi",    "video/x-msvideo"},
            {".bin",    "application/octet-stream"},
            {".bmp",    "image/bmp"},
            {".c",  "text/plain"},
            {".class",  "application/octet-stream"},
            {".conf",   "text/plain"},
            {".cpp",    "text/plain"},
            {".doc",    "application/msword"},
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls",    "application/vnd.ms-excel"},
            {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe",    "application/octet-stream"},
            {".gif",    "image/gif"},
            {".gtar",   "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h",  "text/plain"},
            {".htm",    "text/html"},
            {".html",   "text/html"},
            {".jar",    "application/java-archive"},
            {".java",   "text/plain"},
            {".jpeg",   "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log",    "text/plain"},
            {".m3u",    "audio/x-mpegurl"},
            {".m4a",    "audio/mp4a-latm"},
            {".m4b",    "audio/mp4a-latm"},
            {".m4p",    "audio/mp4a-latm"},
            {".m4u",    "video/vnd.mpegurl"},
            {".m4v",    "video/x-m4v"},
            {".mov",    "video/quicktime"},
            {".mp2",    "audio/x-mpeg"},
            {".mp3",    "audio/x-mpeg"},
            {".mp4",    "video/mp4"},
            {".mpc",    "application/vnd.mpohun.certificate"},
            {".mpe",    "video/mpeg"},
            {".mpeg",   "video/mpeg"},
            {".mpg",    "video/mpeg"},
            {".mpg4",   "video/mp4"},
            {".mpga",   "audio/mpeg"},
            {".msg",    "application/vnd.ms-outlook"},
            {".ogg",    "audio/ogg"},
            {".pdf",    "application/pdf"},
            {".png",    "image/png"},
            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop",   "text/plain"},
            {".rc", "text/plain"},
            {".rmvb",   "audio/x-pn-realaudio"},
            {".rtf",    "application/rtf"},
            {".sh", "text/plain"},
            {".tar",    "application/x-tar"},
            {".tgz",    "application/x-compressed"},
            {".txt",    "text/plain"},
            {".wav",    "audio/x-wav"},
            {".wma",    "audio/x-ms-wma"},
            {".wmv",    "audio/x-ms-wmv"},
            {".wps",    "application/vnd.ms-works"},
            {".xml",    "text/plain"},
            {".z",  "application/x-compress"},
            {".zip",    "application/x-zip-compressed"},
            {"",        "*/*"}
    };
}
