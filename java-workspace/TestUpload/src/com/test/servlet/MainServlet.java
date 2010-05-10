package com.test.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jspsmart.upload.File;
import com.jspsmart.upload.Files;
import com.jspsmart.upload.SmartUpload;

public class MainServlet extends HttpServlet {
    
    private ServletConfig config;
    
    /**
    * Init the servlet
    */
    final public void init(ServletConfig config) throws ServletException {
        this.config = config;
    }
    
    final public ServletConfig getServletConfig() {
        return config;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        SmartUpload smartUpload = new SmartUpload();
        try {
	        smartUpload.initialize(this.config, request, response);
	        smartUpload.upload();
            Files files = smartUpload.getFiles();
            File file = files.getFile(0);
	        String pathname = super.getServletContext().getRealPath("/");
	        smartUpload.save(pathname);
            request.setAttribute("filename", pathname + file.getFileName());
            String url = request.getScheme() + "://" + request.getServerName() + ":" + 
            request.getLocalPort() + request.getContextPath() + "/" + file.getFileName();
            request.setAttribute("urlname", url);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }

}
