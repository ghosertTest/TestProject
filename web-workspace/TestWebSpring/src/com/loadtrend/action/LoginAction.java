package com.loadtrend.action;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.loadtrend.info.LoginInfo;

public class LoginAction extends SimpleFormController {
    
    private String fail_view;

    private String success_view;

    protected ModelAndView onSubmit(Object cmd, BindException ex)
            throws Exception {
        LoginInfo loginInfo = (LoginInfo) cmd;
        if (login(loginInfo) == 0) {
            HashMap result_map = new HashMap();
            result_map.put("logininfo", loginInfo);
            List msgList = new LinkedList();
            msgList.add("msg1");
            msgList.add("msg2");
            msgList.add("msg3");
            result_map.put("messages", msgList);
            return new ModelAndView(this.getSuccess_view(), result_map);
        } else {
        	int error = 1/0;
        	System.out.println( error + ": The page will be redirected to the page showError.jsp, Because of " + 
        			            "the exceptionResolver id in Config.xml" );
            return new ModelAndView(this.getFail_view());
        }
    }

    private int login(LoginInfo loginInfo) {
        if ("jiawei".equalsIgnoreCase(loginInfo.getUsername())
                && "1234".equals(loginInfo.getPassword())) {
            return 0;
        }
        return 1;
    }

    public String getFail_view() {
        return fail_view;
    }

    public String getSuccess_view() {
        return success_view;
    }

    public void setFail_view(String fail_view) {
        this.fail_view = fail_view;
    }

    public void setSuccess_view(String success_view) {
        this.success_view = success_view;
    }
    
    
}
