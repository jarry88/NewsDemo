package com.example.newsdemoapplication;

import org.jetbrains.annotations.Nullable;

public class Constants {


    /**
     * bugly APPID
     */
    public static final String BUGLY_APP_ID = "75fff84342";
    public static final String APPID = "WF001";
//    public static final String CHANNEL_CODE = "Hdcash";
    public static final String CHANNEL_CODE = "Wifi";
    public static final String APP_PACKAGENAME= "com.gzp.newsapp";
    public static final String ENCRYPTKEY= "ENCRYPTKEY";
    public static final String ENCRYPTTYPE= "encryptType";
    public static final String FK_KEY = "app-key-biz-001";
    public static final String PRIVACY_POLICY_URL = "https://static.cashget.im/privacy-policy/privacy&policy.html";
    public static final String TERMS_SERVICES_URL = "https://static.cashget.im/privacy-policy/team.html";

    public static final String LoginPath = "/login/login";
    public static final String MainPath = "/main/main";
    public static final String BaseWebViewPath = "/base/webview";

    //personinfo_module 模块下
    public static final String PersonInfoPath = "/person/personinfo";
    public static final String PersonCreatApplyPreOrderPath = "/person/creat_apply_pre_order";
    public static final String LivenessPath ="/person/liveness";
    public static final String LivenessResultPath= "/person/liveness_result";
    public static final String AadhaarCardPath ="/person/aadhaar_card";
    public static final String IdentityVerificationFailedPath ="/person/identity_verification";
    public static final String AadhaarCardBackTakePhotoPath = "/person/aadhaar_card_back_take_photo";
    public static final String AadhaarCardBackTakePhotoPreviewPath =  "/person/aadhaar_card_back_take_photo_preview";
    public static final String AadhaarCardBackSelectPhotoPath = "/person/aadhaar_card_back_select_photo";
    public static final String AadhaarCardBackSelectPhotoPreviewPath =  "/person/aadhaar_card_back_select_photo_preview";
    public static final String AadhaarCardFrontTakePhotoPath = "/person/aadhaar_card_front_take_photo";
    public static final String AadhaarCardFrontTakePhotoPreviewPath =  "/person/aadhaar_card_front_take_photo_preview";
    public static final String AadhaarCardFrontSelectPhotoPath ="/person/aadhaar_card_front_select_photo";
    public static final String AadhaarCardFrontSelectPhotoPreviewPath =  "/person/aadhaar_card_front_select_photo_preview";
    public static final String PanCardFrontTakePhotoPath = "/person/pan_card_front_take_photo";
    public static final String PanCardFrontTakePhotoPreviewPath =  "/person/pan_card_front_take_photo_preview";
    public static final String PanCardFrontSelectPhotoPath ="/person/pan_card_select_photo";
    public static final String PanCardFrontSelectPhotoPreviewPath =  "/person/pan_card_select_photo_preview";
    public static final String BindIfscBankAccountPath ="/person/bind_ifsc_bank_account";
    public static final String BindBankAccountPath ="/person/bind_bank_account";
    public static final int AADHAAR_CARD_FRONT=0;
    public static final int AADHAAR_CARD_BACK=1;
    public static final int PAN_CARD_FRONT=2;
    public static final String PICTYPE_AADHAAR="AADHAAR";
    public static final String PICTYPE_PAN="PAN";
    public static final String PICTYPE_FACE="FACE";
    public static final int TAKE_PHOTO=1;
    public static final int SELECT_PHOTO=2;
    public static final String UPLOAD_PAN_SUCCESS="UPLOAD_PAN_SUCCESS";
    public static final String UPLOAD_PAN_FAIL="UPLOAD_PAN_FAIL";
    public static final String UPLOAD_ADDHAAR_CARD_SUCCESS="UPLOAD_ADDHAAR_CARD_SUCCESS";
    public static final String UPLOAD_ADDHAAR_CARD_FAIL="UPLOAD_ADDHAAR_CARD_FAIL";
    public static final String PICK_ADDHAAR_CARD_SUCCESS="PICK_ADDHAAR_CARD_SUCCESS";
    public static final String RECHOOSE="RECHOOSE";
    public static final int PICK_IMAGE=2000;
    public static final int CAMERA_IMAGE=2001;


    public static final String Customer_Path= "/customer/customer";
    public static final String WriteToUs_Path= "/write/write";
    public static final String FAQ_Path= "/faq/faq";
    public static final String REPAYMENT_LOAD_STATUS_FRAGMENT="/repayment/LoadStatusFragment";
    public static final String REPAYMENT_REPAY="/repayment/repay";
    //mine
    public static final String Setting_Path= "/set/set";
    public static final String Report_Path= "/report/report";
    public static final String Bank_Path= "/bank/bank";
    public static final String ORDER_INFO_PATH= "/order/detail";

    //订单
    public static final String Order_Path= "/order/order";

    /**
     * 登录模块接口规定常亮
     * smsCodeType
     */

    public static final String SMS_CODE_REGISTER= "REGISTER";
    public static final String SMS_CODE_LOGIN= "LOGIN";
    public static final String SMS_CODE_RESET_PASSWORD= "RESET_PASSWORD";//找回密码|重置密码"
    public static final String IS_FIRST_START="IS_FIRST_START";
    public static final String PERMISSIONS_START_PATH="/app/permissions";

    public static final String REGISTERSOURCE="test";
    @Nullable
    public static final String IsEdit="isedit";
    public static final String ChapterVo="chapterVo";
}
