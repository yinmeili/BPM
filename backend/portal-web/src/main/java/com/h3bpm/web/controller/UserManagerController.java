package com.h3bpm.web.controller;

import OThinker.H3.Controller.ControllerBase;
import com.h3bpm.web.entity.User;
import com.h3bpm.web.service.UserService;
import com.h3bpm.web.vo.RespListSubordinateByUserIdVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/Portal/user")
public class UserManagerController extends ControllerBase {

    private static final Logger logger = LoggerFactory.getLogger(UserManagerController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/listSubordinateByUserId", method = RequestMethod.GET, produces = "application/json;charset=utf8")
    @ResponseBody
    public List<RespListSubordinateByUserIdVo> listKnowledgeTagByName() throws IOException {

        List<RespListSubordinateByUserIdVo> list = new ArrayList<>();

        Map<String, Object> userMap = null;
        try {
            userMap = this._getCurrentUser();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        OThinker.Common.Organization.Models.User loginUser = (OThinker.Common.Organization.Models.User) userMap.get("User");

        List<User> subordinateList = userService.findSubordinateByUserId(loginUser.getObjectId());

        list.add(new RespListSubordinateByUserIdVo(loginUser.getObjectId(), loginUser.getName()));
        for (User user : subordinateList) {
            list.add(new RespListSubordinateByUserIdVo(user));
        }

        return list;
    }

    @Override
    public String getFunctionCode() {
        // TODO Auto-generated method stub
        return null;
    }

}
