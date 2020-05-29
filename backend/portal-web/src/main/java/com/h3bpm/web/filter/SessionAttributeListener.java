package com.h3bpm.web.filter;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import com.h3bpm.web.utils.Constants;
import com.h3bpm.web.utils.UserSessionUtils;
import com.h3bpm.web.vo.UserSessionInfo;

/**
 * @author : tonghao
 * @description :
 * @date : Create in 15:17 2018/11/1
 */
@WebListener
public class SessionAttributeListener implements HttpSessionAttributeListener {
    @Override
    //创建session时触发
    public void attributeAdded(HttpSessionBindingEvent event) {
        if ((Constants.SESSION_USER).equals(event.getName())) {
        	UserSessionUtils.set(Constants.SESSION_USER,(UserSessionInfo) event.getValue());
        }
    }

    @Override
    //销毁session时触发
    public void attributeRemoved(HttpSessionBindingEvent event) {
        if ((Constants.SESSION_USER).equals(event.getName())) {
        	UserSessionUtils.remove();
        }
    }

    @Override
    //替换session时触发
    public void attributeReplaced(HttpSessionBindingEvent event) {
        if ((Constants.SESSION_USER).equals(event.getName())) {
        	UserSessionUtils.set(Constants.SESSION_USER,(UserSessionInfo) event.getValue());
        }
    }
}
