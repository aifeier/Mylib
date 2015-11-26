package lib.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.cwf.app.cwf.R;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lib.BaseActivity;

/**
 * Created by n-240 on 2015/11/24.
 */
public class FileExploreActivity extends BaseActivity
        implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    public static final String FILEDIALOG_PATH = "path";
    public static final String FILEDIALOG_NAME = "name";
    public static final String FILEDIALOG_IMG = "img";

    public static final String KEY_ROOT_PATH = "root_path";

    public static final String sRoot = "/";
    public static final String sParent = "..";
    public static final String sFolder = ".";

    private ListView listView;
    private TextView empty_view;

    private Map<String, Integer> images;/*不同文件显示的图片*/
    private String root;/*显示的文件根目录*/
    private String path;/*显示的文件当前目录*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_explore_list);
        setTitle("缓存文件查看");
        initData();
        initView();
    }

    private void initView(){
        empty_view = (TextView) findViewById(R.id.file_empty);
        listView = (ListView) findViewById(R.id.file_list);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        refreshListData();
    }

    private void refreshListData(){
        // 刷新文件列表
        File[] files = null;
        if(!new File(path).exists()){
            /*防止返回时目录不存在报错*/
            ActivityUtils.showTip("目录不存在", false);
            finish();
            return;
        }
        try {
            files = new File(path).listFiles();
        } catch (Exception e) {
            files = null;
        }
        if (files == null) {
            // 访问出错
            ActivityUtils.showTip("访问出错", false);
            finish();
            return;
        }
        List<File> fileList = new ArrayList<>();
        if(!path.equals(root)){
            File file = new File(root);
            fileList.add(file);
            file = new File(path.substring(0, path.lastIndexOf("/")));
            fileList.add(file);
        }
        for(File file : files){
            if(file.exists())
                fileList.add(file);
        }

        Collections.sort(fileList, new FileComparator(3, true));

        listView.setAdapter(new baseAdapter(fileList));
    }

    //通过重写Comparator的实现类FileComparator来实现按文件创建时间排序。
    public class FileComparator implements Comparator<File> {

        /*@order_by 按什么排序
        * @drop 是否降序排列*/
        private int type ;
        private boolean up;
        public FileComparator(int order_by, boolean  up){
            type = order_by;
            this.up = up;
        }
        public int compare(File file1, File file2) {
            /*文件夹显示在前面*/
            if(file1.isDirectory() && file2.isFile())
                return -1;
            else if(file1.isFile() && file2.isDirectory())
                return 1;
            else {
                switch (type) {
                    case 0:
                        /*不排序*/
                        return 1;
                    case 1:
                        /*按时间排序*/
                        if (file1.lastModified() < file2.lastModified()) {
                            return up ? -1 : 1;
                        }  else {
                            return up ? 1 : -1;
                        }
                    case 2:
                        /*按大小排序，文件夹不会排序*/
                        if(file1.length() > file2.length())
                            return up ? -1 : 1;
                        else
                            return up ? 1 : -1;
                    case 3:
                        /*按文件名排序*/
                        if(file1.getName().compareToIgnoreCase(file2.getName()) < 0)
                            return up ? -1 : 1;
                        else
                            return up ? 1 : -1;
                    default:
                        return 1;


                }
            }
        }
    }

    private class baseAdapter extends BaseAdapter {

        private List<File> list;
        public baseAdapter(List<File> list){
            if(list!=null)
                this.list = list;
            else
                this.list = new ArrayList<>();
            if(list==null||(root == path? list.size() == 0 : list.size() == 2)){
                empty_view.setVisibility(View.VISIBLE);
            }else{
                empty_view.setVisibility(View.GONE);
            }
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public File getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView ==null){
                convertView = LayoutInflater.from(FileExploreActivity.this)
                        .inflate(R.layout.file_item, null);
                viewHolder = new ViewHolder();
                viewHolder.file_item_img =
                        (ImageView)convertView.findViewById(R.id.file_item_img);
                viewHolder.file_item_name =
                        (TextView) convertView.findViewById(R.id.file_item_name);
                viewHolder.file_item_time =
                        (TextView) convertView.findViewById(R.id.file_item_time);
                viewHolder.file_item_right=
                        (ImageView) convertView.findViewById(R.id.file_item_right);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            File item = getItem(position);
            if(item.isDirectory())
                Glide.with(FileExploreActivity.this)
                        .load(R.drawable.ic_logo_about)
                        .into(viewHolder.file_item_img);
            else {
                int img = getImageId(
                        getSuffix(item.getName().toLowerCase()));
                if (img != 0)
                    Glide.with(FileExploreActivity.this)
                            .load(img)
                            .into(viewHolder.file_item_img);
                else
                    Glide.with(FileExploreActivity.this)
                            .load(R.drawable.ic_other_filetype)
                            .into(viewHolder.file_item_img);
            }
            if(item.isFile())
                viewHolder.file_item_time.setText(
                        FileSizeUtil.FormetFileSize(item.length()) + " | " +
                                TimeUtils.getSimpleDate(
                                        new Date(item.lastModified())));
            else{
                viewHolder.file_item_time.setText(
                        TimeUtils.getSimpleDate(
                                new Date(item.lastModified())));
            }


            if(!root.equals(path) && (position == 0 || position == 1)){
                if(position == 0){
                    viewHolder.file_item_name.setText("返回主目录");
                }else {
                    viewHolder.file_item_name.setText("返回上级目录");
                }
                viewHolder.file_item_time.setText(item.getPath());
            }else {
                viewHolder.file_item_name.setText(item.getName());
            }

            if(item.isDirectory())
                viewHolder.file_item_right.setVisibility(View.VISIBLE);
            else
                viewHolder.file_item_right.setVisibility(View.GONE);
            if(item.getName().contains(".jpg")||
                    item.getName().contains(".png") ||
                    item.getName().contains(".jpeg")) {
                Glide.with(FileExploreActivity.this)
                        .load(item)
                        .into(viewHolder.file_item_img);
            }else if(item.getName().contains(".mp4")||
                        item.getName().contains(".avi")||
                            item.getName().contains(".mkt")) {
                Glide.with(FileExploreActivity.this)
                        .load(item)
                        .into(viewHolder.file_item_img);
            }else if(item.getName().contains(".mp3")||
                        item.getName().contains(".aac")||
                            item.getName().contains(".flac")){
 /*               viewHolder.file_item_img.setImageBitmap(
                        MediaThumbnailUtils.createAlbumThumbnail(item.getPath()));*/
            }
            return convertView;
        }

        private class ViewHolder{
            ImageView file_item_img;
            TextView file_item_name;
            TextView file_item_time;
            ImageView file_item_right;
        }
    };

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
            return images.get(".");
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
        images.put("bmp", R.drawable.ic_picture_filetype);
        images.put("jpeg", R.drawable.ic_picture_filetype);
        images.put("jpg", R.drawable.ic_picture_filetype);
        images.put("png", R.drawable.ic_picture_filetype);
        images.put("doc", R.drawable.ic_document_filetype);
        images.put("docx", R.drawable.ic_document_new);
        images.put("ppt", R.drawable.ic_ppt_filetype);
        images.put("pptx", R.drawable.ic_ppt_filetype);
        images.put("xls", R.drawable.ic_excel_filetype);
        images.put("xlsx", R.drawable.ic_excel_new);
        images.put("txt", R.drawable.ic_txt_filetype);
        images.put("wps", R.drawable.ic_document_filetype);
        images.put("log", R.drawable.ic_txt_filetype);
        images.put("apk", R.drawable.ic_apk_filetype);

        images.put("3gp", R.drawable.ic_video_filetype);
        images.put("avi", R.drawable.ic_video_filetype);
        images.put("mp4", R.drawable.ic_video_filetype);
        images.put("mkt", R.drawable.ic_video_filetype);
        images.put("wmv", R.drawable.ic_video_filetype);
        images.put("asf", R.drawable.ic_video_filetype);
        images.put("mov", R.drawable.ic_video_filetype);
        images.put("mpeg", R.drawable.ic_video_filetype);
        images.put("mpg", R.drawable.ic_video_filetype);
        images.put("mpe", R.drawable.ic_video_filetype);
        images.put("mpg4", R.drawable.ic_video_filetype);
        images.put("mpga", R.drawable.ic_video_filetype);



        images.put("mp3", R.drawable.ic_audio_filetype);
        images.put("flac", R.drawable.ic_audio_filetype);
        images.put("aac", R.drawable.ic_audio_filetype);
        images.put("m4c", R.drawable.ic_audio_filetype);
        images.put("dms", R.drawable.ic_audio_filetype);
        images.put("m4a", R.drawable.ic_audio_filetype);
        images.put("m4b", R.drawable.ic_audio_filetype);
        images.put("m4p", R.drawable.ic_audio_filetype);
        images.put("mp2", R.drawable.ic_audio_filetype);
        images.put("ogg", R.drawable.ic_audio_filetype);




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
                refreshListData();
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
            {".flac",    "audio/x-mpeg"},
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // select item
        final File item = (File) listView.getAdapter().getItem(position);
        String pt = item.getPath();
        String fn = item.getName();
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
            if (item.isFile()) {
                openFile(item);
                return;
            } else if (item.isDirectory()) {
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        // select item
        final File item = (File) listView.getAdapter().getItem(position);
        String pt = item.getPath();
        String fn = item.getName();
        if(!fn.equals(sParent)&&!fn.equals(root)) {
            if (item.isFile()) {
/*                new BaseDialog.Builder(this)
                        .setTitle("删除文件")
                        .addCloseIcon()
                        .setMessage("是否确认删除文件：" + item.getName())
                        .addPositiveButton(this.getString(R.string.confirm),
                                new BaseDialog.Builder.OnDialogButtonClickListener() {
                                    @Override
                                    public boolean onDialogButtonClick(View view) {
                                        item.delete();
                                        refreshListData();
                                        return false;
                                    }
                                }).addNegativeButton(this.getString(R.string.cancel), null).show();
   */         }
        }
        refreshListData();
        return false;
    }
}
