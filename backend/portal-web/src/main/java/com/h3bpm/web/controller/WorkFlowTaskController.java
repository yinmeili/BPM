package com.h3bpm.web.controller;

import com.h3bpm.web.service.WorkFlowService;
import com.h3bpm.web.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping(value = "/workflowTask")
public class WorkFlowTaskController {

    @Autowired
    private WorkFlowService workFlowService;

    @RequestMapping(value = "/importTask", produces = "application/json;charset=utf8")
    @ResponseBody
    public ResponseVo importExcel(@RequestParam("file") MultipartFile file){
        workFlowService.importExcel(file);
        return new ResponseVo("导入成功");
    }

}
