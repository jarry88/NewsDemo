package com.gzp.baselib.base

import androidx.lifecycle.ViewModel
import org.json.JSONObject


open class BaseViewModel : ViewModel() {

//
//    /**
//     * app/api/multi/v2/user/aadhaar-pan-check 查询实名状态
//     */
//    fun aadhaarPanCheck(token: String, data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "app/api/multi/v2/user/aadhaar-pan-check", token, data, observer)
//    }
//
//    /**
//     * app/api/multi/v2/userApplyState 获取用户借款状态信息接口
//     */
//    fun userApplyState(token: String, data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "app/api/multi/v2/userApplyState", token, data, observer)
//    }
//
//    /**
//     * app/api/multi/v2/getAppConfigInfo 获取用户
//     */
//    fun getAllAppConfigInfo(token: String, data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "app/api/multi/v2/getAppConfigInfo", token, data, observer)
//    }
//    /**
//     * app/api/multi/v2/getAppConfigInfo 获取加密方式
//     */
//    fun getAllAppConfigInfo(data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postNoToken(BuildConfig.BASEURL + "app/api/multi/v2/getAppConfigInfo" ,data, observer)
//    }
//
//    /**
//     * app/api/multi/v2/resetUserOCRInfo 获取用户
//     */
//    fun resetUserOCRInfo(token: String, data: JSONObject, observer: ResultBooleanObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "app/api/multi/v2/resetUserOCRInfo", token, data, observer)
//    }
//
//
//
//    fun getOrderDetail(token: String, jsonObject: HashMap<String, String>, observer: ResultJSONObjectObserver) {
//        HttpManager.getTokenRequest(BuildConfig.BASEURL + "app/api/multi/v2/user/getOrderDetail", token, jsonObject, observer)
//    }
//
//    /**
//     * app/api/multi/v2/user/getPreOrderStatus
//     */
//    fun getPreOrderStatus(token: String, data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "app/api/multi/v2/user/getPreOrderStatus", token, data, observer)
//    }
//
//
//    fun getOrderDetail(token: String,  observer: ResultJSONObjectObserver) {
//        HttpManager.getTokenNoParamRequest(BuildConfig.BASEURL + "app/api/multi/v2/user/getOrderDetail", token,  observer)
//    }
//
//    //en-US，hi-IN
//    fun getCreditLevel(token: String, data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "app/api/multi/v2/get-member-level", token, data, observer)
//    }
//
//    fun getProductInfo(token: String, data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "app/api/multi/v2/getProductInfo", token, data, observer)
//    }
//    //en-US，hi-IN
//    fun getAuthenState(token: String, data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "app/api/multi/v2/user/getAuthenState", token, data, observer)
//    }
//    fun userRepay(token: String, data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "app/api/multi/v2/userRepay", token, data, observer)
//    }
//    fun extensionRepaymentFee(token: String, data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "api/multi/v2/extension-repayment-fee", token, data, observer)
//    }
//    fun extensionRepaymentSubmit(token: String, data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "api/multi/v2/extension-repayment-submit", token, data, observer)
//    }
//    fun extensionRepaymentCalc(token: String, data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "api/multi/v2/extension-repayment-calc", token, data, observer)
//    }
//    fun submitUserRepayResult(token: String, data: JSONObject, observer: ResultJSONObjectObserver) {
//        HttpManager.postTokenRequest(BuildConfig.BASEURL + "api/multi/v2/submitUserRepayResult", token, data, observer)
//    }
//
//    fun getRequestHasParam(url:String,token: String, data:Map<String,String>, observer: ResultJSONObjectObserver) {
//        HttpManager.getRequestHasParam(url, token, data, observer)
//    }
}