package com.mini.sdk.utils;

public class Constant {

    private static Constant instance;

    public static Constant getInstance() {
        if (null == instance) {
            instance = new Constant();
        }
        return instance;
    }

    private Constant() {
    }

        public static final String IP = "http://nianwan.cn/sdk.php/";
//    public static final String IP = "http://test.nianwan.cn/sdk.php/";


    /**
     * 初始化悬浮窗口
     */
    public static final String INIT_FLOATING_URL = IP + "game/get_suspend";

    /**
     * 是否快捷注册（初始化）
     */
    public static final String IS_QUICK_LOGIN = IP + "/user/is_quick_login";
    /**
     * 登陆
     */
    public static final String USER_LOGIN = IP + "User/user_login";
    /**
     * 普通注册
     */
    public static final String USER_REGISTER = IP + "User/user_register";
    /**
     * 游客登陆
     */
    public static final String VISITOR_LOGIN = IP + "User/oauth_login";
    /**
     * 手机号注册
     */
    public static final String USER_PHONE_REGISTER = IP + "User/user_phone_register";

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 用户信息
     */
    public static final String USER_INFO_URL = IP + "User/user_info";

    /**
     * 获得手机验证码
     */
    public static final String verify_Phone_Code = IP + "User/sendSms";
    /**
     * 验证手机验证码
     */
    public static final String CHECK_PHONE_CODE = IP + "User/verify_sms";
    /**
     * 验证手机号是否存在
     */
    public static final String check_account = Constant.IP + "/User/checkAccount";
    /**
     * 忘记密码
     */
    public static final String FORGET_PASSWORD = IP + "User/forget_password";
    /**
     * 获取支付列表
     */
    public static final String payType_list = IP + "user/get_pay_server";

    /**
     * 支付宝订单信息
     */
    public static final String alipay_order_url = IP + "Pay/alipay_pay";
    /**
     * 支付之后验证
     */
    public static final String pay_result_verification_url = IP + "Pay/pay_validation";

    /**
     * 微信订单信息
     */
    public static final String wx_order_url = IP + "WapPay/weixin_pay";
    /**
     * 微信支付接口
     */
    public static final String wx_pay_url = IP + "pay/wx_pay";
    /**
     * 微信官方支付结果
     */
    public static final String wx_pay_result_url = IP + "WapPay/get_orderno_restart";

    /**
     * 获取用户平台币
     */
    public static final String query_user_ptb_url = IP + "User/user_platform_coin";
    /**
     * 平台币支付
     */
    public static final String ptb_order_url = IP + "Pay/platform_coin_pay";
    /**
     * 威富通请求订单
     */
    public static final String wftOrderInfoUrl = IP + "Pay/outher_pay";

    /**
     * 通知游戏后台支付成功
     */
    public static final String noticeGamePaySuccess = IP + "GameNotify/game_pay_notify";
    /**
     * 用户在该游戏中的礼包列表(获得礼包列表)
     */
    public static final String game_packages_list = IP + "GameGift/gift_list";

    /**
     * 游戏礼包码
     */
    public static final String game_packages_code_url = IP + "GameGift/receive_gift";

    /**
     * 绑定手机
     */
    public static final String update_user_info_url = IP + "User/user_update_data";
    /**
     * 解绑手机
     */
    public static final String user_unbind_phone_url = IP + "User/user_phone_unbind";

    /**
     * logo
     */
    public static final String download_logo_url = IP.substring(0, IP.indexOf("sdk.php/")) + "Public/Sdk/logo.png";

    /**
     * 实名认证
     */
    public static final String certificate = IP + "user/idcard_change";

    /**
     * 获取年纪
     */
    public static final String ANTIADDICTION_URL = IP + "user/return_age";

    /**
     * 用户注册协议链接
     */
    public static final String user_agreement_url = IP.substring(0, IP.indexOf("sdk.php/")) + "media" + ".php?s=/Article/agreement.html";
    /**
     * 用户支付链接
     */
    public static final String pay_agreement_url = IP.substring(0, IP.indexOf("sdk.php/")) + "media" + ".php?s=/Article/payAgreement.html";

    /**
     * 获取客服QQ
     */
    public static final String service_url = IP + "Game/get_game_ccustom_service_qq";
    /**
     * 上传角色
     */
    public static final String upload_role_url = IP + "User/save_user_play_info";

    /**
     * 游戏充值记录
     */
    public static final String game_recode_url = IP + "Spend/spend_recond_list";
    /**
     * 平台币充值记录
     */
    public static final String add_ptb_record_url = IP + "User/user_deposit_record";

    /**
     * 获得第三方登录需要的参数，qqappid等
     */
    public static final String third_login_url = IP + "user/oauth_param";
    /**
     * 第三方登录请求
     */
    public static final String third_login_request = IP + "user/oauth_login";
    ;

    /**
     * 获取折扣 首冲 续冲
     */
    public static final String user_discount_url = IP + "user/get_user_discount";
    /**
     * 获取折扣 首冲 续冲
     */
    public static final String third_login_type_url = IP + "User/thirdparty";

    /**
     * 俊付通接口
     */
    public static final String jft_order_info_url = IP + "pay/jft_pay";
    /**
     * 汇付宝订单很支付请求
     */
    public static final String hfb_order_info_url = IP + "pay/heepay_pay";

    /**
     * 根据用户uid获取该用户是否实名
     */
    public static final String antiAddictionUrl = IP + "user/return_age";
    /**
     * 下线通知
     */
    public static final String OFF_LINE_ANNOUCE_URL = IP + "user/get_down_time";


    public static final String PRE_NAME = "userInfo";
    public static final String CUSTOMER = "account";
    public static final String PASSWORD = "password";
    public static final String SAVEPWD = "savepwd";
    public static final String CUSTOMER_YK = "ykaccount";
    public static final String GET_ONLINE_TIME = "get_online_time";
    public static final String GET_OFF_TIME = "get_offline_time";
    public static final String GET_EXIST_TIME = "get_exist_time";// 在线时长
    public static final String GET_REST_TIME = "get_rest_time";// 休息时长

    /**
     * 微信appid
     */
    public static String WXAPPID = "";

    /**
     * 6-15位数字或英文字母
     */
    public static final String REGULAR_ACCOUNT = "^[a-zA-Z0-9_]{6,15}$";

    /**
     * 手机号码
     */
    public static final String REGULAR_PHONENUMBER = "^1[0-9]{10}$";

    /**
     * 正则：身份证号码18位
     */
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    public static final String REGEX_ID_CARD15 = "^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$";

    /**
     * 正则: 判断是否是中文人名，支持少数名族
     */
    public static final String REGULAR_NAME = "[\\u4E00-\\u9FA5]{2,5}(?:·[\\u4E00-\\u9FA5]{2,5})*";

    /**
     * 登陆成功
     */
    public static final int LOGIN_SUCCESS = 0x01;

    /**
     * 登陆失败
     */
    public static final int LOGIN_FAIL = 0x02;

    /**
     * 注册成功
     */
    public static final int REGISTER_SUCCESS = 0x03;

    /**
     * 注册失败
     */
    public static final int REGISTER_FAIL = 0x04;

    /**
     * 计费点信息成功
     */
    public static final int PAY_INFO_SUCCESS = 0x05;

    /**
     * 计费点信息失败
     */
    public static final int PAY_INFO_FAIL = 0x06;

    /**
     * 支付宝请求成功
     */
    public static final int ZFB_PAY_VALIDATE_SUCCESS = 0x07;

    /**
     * 支付宝请求失败
     */
    public static final int ZFB_PAY_VALIDATE_FAIL = 0x08;

    /**
     * 支付宝App返回
     */
    public static final int SDK_PAY_FLAG = 0x09;

    /**
     * 支付验证成功
     */
    public static final int VERIFICATION_SUCCESS = 0x10;

    /**
     * 支付验证失败
     */
    public static final int VERIFICATION_FAIL = 0x11;

    /**
     * 透传参数成功
     */
    public static final int PASSTHROUGH_SUCCESS = 0x12;

    /**
     * 透传参数失败
     */
    public static final int PASSTHROUGH_FAIL = 0x13;

    /**
     * 微信订单请求成功
     */
    public static final int WEIXIN_ORDER_INFO_SUCCESS = 0x14;

    /**
     * 微信订单请求失败
     */
    public static final int WEIXIN_ORDER_INFO_FAIL = 0x15;

    /**
     * 平台币支付成功
     */
    public static final int PTB_PAY_SUCCESS = 0x16;

    /**
     * 平台币支付失败
     */
    public static final int PTB_PAY_FAIL = 0x17;

    /**
     * 平台币余额
     */
    public static final int PTB_MONEY_SUCCESS = 0x18;
    public static final int PTB_MONEY_FAIL = 0x19;

    /**
     * 修改密码
     */
    public static final int MODIFY_PASSWORD_SUCCESS = 0x20;
    public static final int MODIFY_PASSWORD_FAIL = 0x21;

    /**
     * 微富通订单获取
     */
    public static final int WFT_ORDERINFO_SUCCESS = 0x22;
    public static final int WFT_ORDERINFO_FAIL = 0x23;

    /**
     * 获取礼包列表
     */
    public static final int GET_PACKS_LIST_SUCCESS = 0x24;
    public static final int GET_PACKS_LIST_FAIL = 0x25;

    /**
     * 获取用户信息
     */
    public static final int GET_USER_INFO_SUCCESS = 0x25;
    public static final int GET_USER_INFO_FAIL = 0x26;

    /**
     * 修改昵称
     */
    public static final int UPDATE_NIKE_SUCCESS = 0x27;
    public static final int UPDATE_NIKE_FAIL = 0x28;

    /**
     * 通过账号查找手机号
     */
    public static final int GET_PHONUM_FROACC_SUCCESS = 0x29;
    public static final int GET_PHONUM_FROACC_FAIL = 0x30;

    /**
     * 获取验证码
     */
    public static final int VERIFYCODE_REQUEST_SUCCESS = 0x31;
    public static final int VERIFYCODE_REQUEST_FAIL = 0x32;

    /**
     * 绑定手机
     */
    public static final int USER_BIND_PHONE_SUCCESS = 0x33;
    public static final int USER_BIND_PHONE_FAIL = 0x34;

    /**
     * 绑定手机
     */
    public static final int NOTICE_ADDPTB_SUCCESS = 0x35;
    public static final int NOTICE_ADDPTB_FAIL = 0x36;

    /**
     * 获取充值记录
     */
    public static final int RECORD_LIST_SUCCESS = 0x37;
    public static final int RECORD_LIST_FAIL = 0x38;

    /**
     * 获取礼包码
     */
    public static final int PACKS_CODE_SUCCESS = 0x39;
    public static final int PACKS_CODE_FAIL = 0x40;

    /**
     * 获取游戏充值记录
     */
    public static final int GAME_RECODE_SUCCESS = 0X41;
    public static final int GAME_RECODE_FAIL = 0X42;
    /**
     * 聚宝云支付成功
     */
    public static final int JBY_PAY_REQUEST_SUCCESS = 0x43;

    /**
     * 微信第三方登录之前获得微信appid
     */
    public static final int USER_GET_WXAPPID_SUCCESS = 0x100;
    public static final int USER_GET_WXAPPID_FAIL = 0X101;

    /**
     * 获得第三方登录需要的参数
     */
    public static final int USER_GET_PARAMS_SUCCESS = 0x44;
    public static final int USER_GET_PARAMS_FAIL = 0x45;

    /**
     * 游客登录
     */
    public static final int VISITORS_LOGIN_SUCCESS = 0x102;
    public static final int VISITORS_LOGIN_FAIL = 0x103;

    /**
     * 汇付宝订单请求成功
     */
    public static final int HFB_PAY_REQUEST_SUCCESS = 0x48;
    public static final int HFB_PAY_REQUEST_FAIL = 0x49;

    /**
     * 游戏支付方式
     */
    public static final int GAME_PAY_TYPE_SUCCESS = 0x50;
    public static final int GAME_PAY_TYPE_FAIL = 0x51;

    /**
     * 获取礼包列表
     */
    public static final int GET_USER_DISCOUNT_SUCCESS = 0x52;
    public static final int GET_USER_DISCOUNT_FAIL = 0x53;

    /**
     * 更新游客信息（账户、密码）
     */
    public static final int UPDATE_VISITOR_INFO_SUCCESS = 0x53;
    public static final int UPDATE_VISITOR_INFO_FAIL = 0x54;

    /**
     * 获取第三方登录的方式
     */
    public static final int THIRD_LOGIN_TYPE_SUCCESS = 0x53;
    public static final int THIRD_LOGIN_TYPE_FAIL = 0x54;

    /**
     * 实名认证
     */
    public static final int TO_CERTIFICATE_SUCCESS = 0x58;
    public static final int TO_CERTIFICATE_FAIL = 0x59;

    /**
     * 请求防沉迷信息成功
     */
    public static final int REQUEST_ANTI_ADDICTION_SUCCESS = 0x60;

    /**
     * 请求防沉迷信息失败
     */
    public static final int REQUEST_ANTI_ADDICTION_FAIL = 0x61;

    /**
     * 下线通知成功
     */
    public static final int OFFLINE_SUCCESS = 0x62;

    /**
     * 下线通知失败
     */
    public static final int OFFLINE_FAIL = 0x63;

    /**
     * 检查验证码
     */
    public static final int IS_CODE_SUCCESS = 0x64;
    public static final int IS_CODE_FAIL = 0x65;

    /**
     * 上传角色成功
     */
    public static final int UPLOAD_ROLE_SUCCESS = 0x66;

    /**
     * 上传角色失败
     */
    public static final int UPLOAD_ROLE_FAIL = 0x67;

    /**
     * 获取微信官方支付结果
     */
    public static final int GET_WX_PAY_RESULT_SUCCESS = 0x68;
    public static final int GET_WX_PAY_RESULT_FAIL = 0x69;

    /**
     * 初始化悬浮窗
     */
    public static final int INIT_FLOATING_SUCCESS = 0x70;
    public static final int INIT_FLOATING_FAIL = 0x71;

    // ============================================================================
    // ============================================================================

    private int game_id;
    private String game_name;
    private String game_appid;
    private String sign_key;

    public int getGameId() {
        return game_id;
    }

    public void setGameId(int game_id) {
        this.game_id = game_id;
    }

    public String getGameName() {
        return game_name;
    }

    public void setGameName(String game_name) {
        this.game_name = game_name;
    }

    public String getGameAppId() {
        return game_appid;
    }

    public void setGameAppId(String game_appid) {
        this.game_appid = game_appid;
    }

    public String getSignKey() {
        return sign_key;
    }

    public void setSignKey(String sign_key) {
        this.sign_key = sign_key;
    }
}
