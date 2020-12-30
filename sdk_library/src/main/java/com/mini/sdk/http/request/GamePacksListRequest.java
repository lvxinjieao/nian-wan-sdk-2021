package com.mini.sdk.http.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mini.sdk.entity.GamePackInfo;
import com.mini.sdk.entity.PacksInfo;
import com.mini.sdk.utils.Constant;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.Logs;
import com.mini.sdk.utils.TimeUtil;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public class GamePacksListRequest {

    Handler mHandler;

    public GamePacksListRequest(Handler mHandler) {
        if (null != mHandler) {
            this.mHandler = mHandler;
        }
    }

    public void post(String url, String params) {
        if (TextUtils.isEmpty(url) || null == params) {
            Logs.e("fun#post url is null add params is null");
            noticeResult(Constant.GET_PACKS_LIST_FAIL, "参数为空");
            return;
        }

        HttpUtils.getInstance().httpPost(url, params, new HttpCallback() {
            @Override
            public void requestSuccess(String result) {
                PacksInfo packsInfo = new PacksInfo();

                try {
                    JSONObject json = new JSONObject(result);
                    int status = json.optInt("status");

                    if (status == 200 || status == 1) {
                        packsInfo.setStatus("1");
                        List<GamePackInfo> packInfoList = getGamePackList(json);
                        packsInfo.setPackInfoList(packInfoList);
                        noticeResult(Constant.GET_PACKS_LIST_SUCCESS, packsInfo);
                    } else {
                        String msg = json.optString("msg");
                        if (TextUtils.isEmpty(msg)) {
                            msg = "服务器异常";
                        }
                        Logs.e("msg:" + msg);
                        noticeResult(Constant.GET_PACKS_LIST_FAIL, msg);
                    }
                } catch (JSONException e) {
                    noticeResult(Constant.GET_PACKS_LIST_FAIL, "解析数据异常");
                    Logs.e("fun#get json e = " + e);
                } catch (Exception e) {
                    noticeResult(Constant.LOGIN_FAIL, "解析数据异常");
                }
            }

            @Override
            public void requestFailure(IOException e) {
                Logs.e("onFailure" + e);
                noticeResult(Constant.GET_PACKS_LIST_FAIL, "网络异常");

            }
        });



    }

    protected List<GamePackInfo> getGamePackList(JSONObject json) {
        if (null == json) {
            return null;
        }
        List<GamePackInfo> packInfoList = new ArrayList<GamePackInfo>();
        try {
            JSONArray jsonArray = json.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {// 遍历JSONArray
                GamePackInfo packInfo = new GamePackInfo();

                JSONObject jsonPack = jsonArray.getJSONObject(i);
                packInfo.setId(jsonPack.optString("id"));
                packInfo.setPackName(jsonPack.optString("giftbag_name"));

//				packInfo.setGameId(jsonPack.optString("game_id"));
//				packInfo.setGameName(jsonPack.optString("game_name"));
//				packInfo.setPackCode(jsonPack.optString("novice"));
                packInfo.setPackDesc(jsonPack.optString("desribe"));
                packInfo.setPackImageUrl(jsonPack.optString("icon"));
//				packInfo.setPackStatus(jsonPack.optString("status"));

                packInfo.setPackStatus(jsonPack.optString("received"));
                String mEndTime = jsonPack.optString("end_time");
                if (!mEndTime.equals("0")) {
                    packInfo.setStartTime(TimeUtil.timedate3(jsonPack.optString("start_time")));
                    packInfo.setEndTime(TimeUtil.timedate3(jsonPack.optString("end_time")));
                } else {
                    packInfo.setEndTime("0");
                }
//                long startTime = stringToLong(jsonPack.optString("start_time"));
//                long endTime = stringToLong(jsonPack.optString("end_time"));
//                long nowTime = stringToLong(jsonPack.optString("now_time"));
//                long day = endTime - nowTime;
//                if (day < 0 || (endTime - startTime) < 0) {
//                    packInfo.setEffectiveDates("0");
//                } else {
//                    int dayInt = Integer.parseInt(String.valueOf(day / (3600 * 24)));
//                    packInfo.setEffectiveDates(String.valueOf(dayInt));
//                }

                packInfoList.add(packInfo);
                Logs.w("fun#getGamePackList  packInfo = " + packInfo.toString());
            }
        } catch (JSONException e) {
        }

        return packInfoList;
    }

    private long stringToLong(String str) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        long time;
        try {
            time = Long.parseLong(str);
        } catch (NumberFormatException e) {
            time = 0;
        }
        return time;
    }

    private void noticeResult(int type, Object obj) {
        Message msg = new Message();
        msg.what = type;
        msg.obj = obj;

        if (null != mHandler) {
            mHandler.sendMessage(msg);
        }
    }

}
