package com.mini.sdk.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class FileUtil {

    private String channel_game_string;

    public FileUtil() {
        channel_game_string = getStr();
        //  channelAndChannemStr = "{\"game_id\":\"58\",\"game_name\":\"\u96ea\u5200\u7fa4\u4fa0\u4f20(\u5b89\u5353\u7248)\",\"game_appid\":\"E7EF0AB66B7FEDBFF\",\"promote_id\":\"0\",\"promote_account\":\"\u7cfb\u7edf\"}";
    }

    public String getGameId() {
        return getValueByKey("game_id");
    }

    public String getGameName() {
        return getValueByKey("game_name");
    }

    public String getGameAppId() {
        return getValueByKey("game_appid");
    }

    public String getPromoteId() {
        return getValueByKey("promote_id");
    }

    public String getPromoteAccount() {
        return getValueByKey("promote_account");
    }

    private String getValueByKey(String key) {
        if (TextUtils.isEmpty(channel_game_string)) {
            return "";
        }
        String valueStr = "";
        try {
            JSONObject js = new JSONObject(channel_game_string);
            valueStr = js.getString(key);
            Logs.w(key + ":" + valueStr);
        } catch (JSONException e) {
            valueStr = "";
        }
        return valueStr;
    }

    /**
     * 返回文件中字符串
     */
    private String getStr() {
        InputStream is = null;
        BufferedReader reader = null;
        String result = "";
        StringBuilder sb = new StringBuilder();
        String line = "";
        try {
            String path = "/META-INF/mch.properties";
            is = this.getClass().getResourceAsStream(path);
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            result = "";
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
        return result;
    }

    //==============================================================================================================
    //==============================================================================================================
    //==============================================================================================================
    //==============================================================================================================
    //==============================================================================================================
    public static void writeTxtToFile(String content, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = content + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Logs.d("Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Logs.e("Error on write File:" + e);
        }
    }

    //生成文件
    public static File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    //生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Logs.i(e + "");
        }
    }

    //读取指定目录下的所有TXT文件的文件内容
    public static String getFileContent(File file) {
        String content = "";
        if (!file.isDirectory()) {  //检查此路径名的文件是否是一个目录(文件夹)
            if (file.getName().endsWith("txt")) {//文件格式为""文件
                try {
                    InputStream instream = new FileInputStream(file);
                    if (instream != null) {
                        InputStreamReader inputreader = new InputStreamReader(instream, "UTF-8");
                        BufferedReader buffreader = new BufferedReader(inputreader);
                        String line = "";
                        //分行读取
                        while ((line = buffreader.readLine()) != null) {
                            content += line;
                        }
                        instream.close();//关闭输入流
                    }
                } catch (java.io.FileNotFoundException e) {
                    Logs.d("The File doesn't not exist.");
                } catch (IOException e) {
                    Logs.d(e.getMessage());
                }
            }
        }
        return content;
    }

    //2、调用 - 写入
	//FileUtils.writeTxtToFile(idPASideBase64, "/sdcard/min/", "android.txt");

    //3、调用 - 读取
	//String idPASideBase64 = FileUtils.getFileContent(new File("/sdcard/min/android.txt"));
}
