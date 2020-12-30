package com.mini.sdk.http.check;

import java.util.HashMap;
import java.util.Map;

import com.mini.sdk.bean.ChannelAndGame;
import com.mini.sdk.utils.HttpCallback;
import com.mini.sdk.utils.HttpUtils;
import com.mini.sdk.utils.RequestParamUtil;

/**
 * 验证手机账号是否存在
 */
public class CheckCodeRequest {

	public String phonenum;
	public String phonecode;

	public CheckCodeRequest(String phonenum, String phonecode) {
		this.phonenum = phonenum;
		this.phonecode = phonecode;
	}

	public void post(String url, HttpCallback callback) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", phonecode);
		map.put("phone", phonenum);
		map.put("game_id", ChannelAndGame.getInstance().getGameId());

		String param = RequestParamUtil.getRequestParamString(map);

		HttpUtils.getInstance().httpPost(url, param, callback);
	}



//    HttpCallback callback = new HttpCallback() {
//
//        @Override
//        public void requestSuccess(String result) {
//            Logs.i("响应结果1 : " + result);
//
//            result = Base64Util.decode(result);
//            Logs.i("响应结果2 : " + result);
//            JSONObject json = null;
//            String status = "";
//            String return_code = "";
//            String return_msg = "";
//
//            try {
//                json = new JSONObject(result);
//                status = json.optString("status");
//                return_code = json.optString("return_code");
//                return_msg = json.optString("return_msg");
//            } catch (JSONException e1) {
//                e1.printStackTrace();
//            }
//
//            switch (1) {
//                case 1:
//                    if ("0".equals(status)) {// 验签失败
//                        Toast.makeText(context, return_msg, Toast.LENGTH_LONG).show();
//                    } else if ("1".equals(status)) {// 可以注册
//
//                    } else if ("2".equals(status)) {
//                        Toast.makeText(context, return_msg, Toast.LENGTH_LONG).show();
//                    } else if ("-1".equals(status)) {
//                        Toast.makeText(context, return_msg, Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(context, "不可以注册", Toast.LENGTH_LONG).show();
//                    }
//                    break;
//                case 2:
//                    if ("0".equals(status)) {// 验签失败
//                        Toast.makeText(context, return_msg, Toast.LENGTH_LONG).show();
//                    } else if ("1".equals(status)) {// 数据获取失败
//
//                    } else if ("2".equals(status)) {
//                        Toast.makeText(context, return_msg, Toast.LENGTH_LONG).show();
//                    } else if ("-1".equals(status)) {
//                        Toast.makeText(context, return_msg, Toast.LENGTH_LONG).show();
//                    } else {
//                        Toast.makeText(context, "不可以注册", Toast.LENGTH_LONG).show();
//                    }
//                    break;
//            }
//        }
//
//        @Override
//        public void requestFailure(IOException e) {
//            Toast.makeText(context, "服务器开小差", Toast.LENGTH_LONG).show();
//        }
//    };

    /**
     *
     RequestCallBack<String> callBack = new RequestCallBack<String>() {

    @Override public void onSuccess(ResponseInfo<String> responseInfo) {
    int resultCode = RequestParamUtil.ResultCode(post);
    String result = RequestParamUtil.Result(responseInfo);
    JSONObject json = null;
    String status = "";
    String return_code = "";
    String return_msg = "";
    try {
    json = new JSONObject(result);
    status = json.optString("status");
    return_code = json.optString("return_code");
    return_msg = json.optString("return_msg");
    } catch (JSONException e1) {
    e1.printStackTrace();
    }
    switch (resultCode) {
    case 1:
    if ("0".equals(status)) {// 验签失败
    Toast.makeText(context, return_msg,Toast.LENGTH_LONG).show();
    } else if ("1".equals(status)) {// 可以注册

    } else if ("2".equals(status)) {
    Toast.makeText(context, return_msg,Toast.LENGTH_LONG).show();
    } else if ("-1".equals(status)) {
    Toast.makeText(context, return_msg,Toast.LENGTH_LONG).show();
    } else {
    Toast.makeText(context, "不可以注册",Toast.LENGTH_LONG).show();
    }
    break;
    case 2:
    if ("0".equals(status)) {// 验签失败
    Toast.makeText(context, return_msg,Toast.LENGTH_LONG).show();
    } else if ("1".equals(status)) {// 数据获取失败

    } else if ("2".equals(status)) {
    Toast.makeText(context, return_msg,Toast.LENGTH_LONG).show();
    } else if ("-1".equals(status)) {
    Toast.makeText(context, return_msg,Toast.LENGTH_LONG).show();
    } else {
    Toast.makeText(context, "不可以注册",Toast.LENGTH_LONG).show();
    }
    break;
    default:
    break;
    }
    }

    @Override public void onFailure(HttpException error, String msg) {
    Logs.e("onFailure " + error.getExceptionCode());
    Toast.makeText(context, "服务器开小差",Toast.LENGTH_LONG).show();
    }
    };
     */
}
