package com.h3bpm.web.utils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.h3bpm.web.entity.WorkFlowTask;


import java.io.*;
import java.util.List;

public class FileUtils {

    public static void main(String[] args) {
        File file = new File("/Users/liubinhui/Desktop/tmp.xlsx");
        importExcel(file);

    }
    public static List<WorkFlowTask> importExcel(File file){
        if(!validate(file)) {
            return null;
        }
        ImportParams importParams = new ImportParams();

        importParams.setTitleRows(0);   //excel中的表格名称的占用行数，0表示没有表格名称，为1表示占用一行，2表示占用两行.....(默认占用0行)
        importParams.setHeadRows(1);    //excel中表格的列名称的占用行数行数，1表示占用1行，2表示占用两行......(默认占用1行)
        importParams.setNeedVerfiy(false);  //是否使用Hibernate Validator对excel中的数据进行检测(默认为false,表示不检测)
        try{
            ExcelImportResult<WorkFlowTask> result = ExcelImportUtil.importExcelMore(file,WorkFlowTask.class,importParams);
            return result.getList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    //对原始文件进行相关的校验
    public static boolean validate(File file){
        System.out.println("file = " + file);
        if(!file.exists()) {
            System.out.println("文件不存在");
            return false;
        }
        String fileName = file.getName();
        System.out.println("fileName = " + fileName);

        if (!(fileName.endsWith("xls") || fileName.endsWith("xlsx") || fileName.endsWith("xlsm") || fileName.endsWith("xlt"))) {
            System.out.println("文件格式不正确");
            return false;
        }
        return true;
    }

}
