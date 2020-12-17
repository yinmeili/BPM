package com.h3bpm.web.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.transaction.TransactionException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.h3bpm.web.enumeration.ErrorCode;
import com.h3bpm.web.service.ServiceException;
import com.h3bpm.web.vo.ResponseVo;

import OThinker.H3.Controller.ControllerBase;

public abstract class AbstractController extends ControllerBase {
	@InitBinder
	public void initBinder(WebDataBinder binder, WebRequest request) {

		// 转换日期
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));// CustomDateEditor为自定义日期编辑器
	}

	/*
	 * ==================== 捕获异常Handler =============================
	 */
	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(TypeMismatchException.class)
	private ResponseVo handleException(TypeMismatchException ex) {
		return new ResponseVo(ErrorCode.FAIL, "对不起，您请求的参数在类型转换时发生错误！");
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(IllegalArgumentException.class)
	private ResponseVo handleException(IllegalArgumentException ex) {
		ex.printStackTrace();
		return new ResponseVo(ErrorCode.FAIL, ex.getMessage());
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(ServiceException.class)
	private ResponseVo handleException(ServiceException ex) {
		ex.printStackTrace();
		return new ResponseVo(ErrorCode.FAIL, ex.getMessage());
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(IOException.class)
	private ResponseVo handleException(IOException ex) {
		ex.printStackTrace();
		return new ResponseVo(ErrorCode.FAIL, "对不起，传输的文件有误！");
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(TransactionException.class)
	private ResponseVo handleException(TransactionException ex) {
		ex.printStackTrace();
		return new ResponseVo(ErrorCode.FAIL, "对不起，数据库连接错误！！");
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(FileNotFoundException.class)
	private ResponseVo handleException(FileNotFoundException ex) {
		ex.printStackTrace();
		return new ResponseVo(ErrorCode.FAIL, "对不起，文件不存在！！");
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	private ResponseVo handleException(Exception ex) {
		ex.printStackTrace();
		return new ResponseVo(ErrorCode.FAIL, "对不起，操作失败，请重新尝试！");
	}
	/*
	 * ==================== END =============================
	 */

}
