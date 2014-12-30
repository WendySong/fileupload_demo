package com.pisx.fileupload.util;

import java.util.List;

import com.pisx.fileupload.bean.ReturnMsg;
import org.springframework.validation.ObjectError;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class ReturnMessageUtil {
	/**
	 * 创建校验性错误消息（ajax用）,校验框架异常用
	 * 
	 * @param errors
	 *            错误集合
	 * @return json串
	 */
	public static String createValidationErrorMsg(List<ObjectError> errors) {
		ReturnMsg errorMsg = new ReturnMsg();
		errorMsg.setCode(Constants.ERROR_CODE);
		for (ObjectError oe : errors) {
			errorMsg.setContent(oe.getDefaultMessage());
		}
		return JSONObject.toJSONString(errorMsg, SerializerFeature.WriteMapNullValue);
	}

	/**
	 * 创建成功的消息（ajax用）
	 * 
	 * @param ro
	 * @return json串
	 */
	public static String createOKMsg(Object ro) {
		ReturnMsg okMsg = new ReturnMsg();
		okMsg.setCode(Constants.CORRECT_CODE);
		okMsg.setContent(ro);
		return JSONObject.toJSONString(okMsg, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
	}

	/**
	 * 创建异常消息
	 * 
	 * @param e
	 * @return
	 */
	public static String createExceptionMsg(Exception e) {
		ReturnMsg okMsg = new ReturnMsg();
		okMsg.setCode(Constants.ERROR_CODE);
		okMsg.setContent(e.getMessage());
		return JSONObject.toJSONString(okMsg, SerializerFeature.WriteMapNullValue);
	}

	/**
	 * 创建校验性错误消息（ajax用）,普通字段用
	 * 
	 * @param msg 错误消息
	 * @return
	 */
	public static String createErrorMsg(String msg) {
		ReturnMsg errorMsg = new ReturnMsg();
		errorMsg.setCode(Constants.ERROR_CODE);
		errorMsg.setContent(msg);
		return JSONObject.toJSONString(errorMsg, SerializerFeature.WriteMapNullValue);
	}
	
	/**
	 * 创建not found消息
	 * @param msg
	 * @return
	 */
	public static String createNotFoundMsg(String msg) {
		ReturnMsg errorMsg = new ReturnMsg();
		errorMsg.setCode(Constants.NOT_FOUND_CODE);
		errorMsg.setContent(msg);
		return JSONObject.toJSONString(errorMsg, SerializerFeature.WriteMapNullValue);
	}
	
	/**
	 * 创建回调方法使用成功的消息
	 * @param ro
	 * @param callback
	 * @return
	 */
	public static String createOKMsg(Object ro, String callback) {
		ReturnMsg okMsg = new ReturnMsg();
		okMsg.setCode(Constants.CORRECT_CODE);
		okMsg.setContent(ro);
		if(callback!=null){
			StringBuffer returnStr = new StringBuffer();
			returnStr.append(callback).append("(").append(JSONObject.toJSONString(okMsg, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect)).append(");");
			return returnStr.toString();
		}else{
			return JSONObject.toJSONString(okMsg, SerializerFeature.WriteMapNullValue, SerializerFeature.DisableCircularReferenceDetect);
		}
	}
	
	/**
	 * 创建回调方法用校验性错误消息
	 * @param msg
	 * @param callback
	 * @return
	 */
	public static String createErrorMsg(String msg, String callback) {
		ReturnMsg errorMsg = new ReturnMsg();
		errorMsg.setCode(Constants.ERROR_CODE);
		errorMsg.setContent(msg);
		if(callback!=null){
			StringBuffer returnStr = new StringBuffer();
			returnStr.append(callback).append("(").append(JSONObject.toJSONString(errorMsg, SerializerFeature.WriteMapNullValue)).append(");");
			return returnStr.toString();
		}else{
			return JSONObject.toJSONString(errorMsg, SerializerFeature.WriteMapNullValue);
		}
	}
	
	/**
	 * 创建回调方法用not found消息
	 * @param msg
	 * @param callback
	 * @return
	 */
	public static String createNotFoundMsg(String msg, String callback) {
		ReturnMsg errorMsg = new ReturnMsg();
		errorMsg.setCode(Constants.NOT_FOUND_CODE);
		errorMsg.setContent(msg);
		if(callback!=null){
			StringBuffer returnStr = new StringBuffer();
			returnStr.append(callback).append("(").append(JSONObject.toJSONString(errorMsg, SerializerFeature.WriteMapNullValue)).append(");");
			return returnStr.toString();
		}else{
			return JSONObject.toJSONString(errorMsg, SerializerFeature.WriteMapNullValue);
		}
	}
}
