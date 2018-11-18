package com.webim;

import javax.servlet.http.*;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServlet;


public class RedirectToAuthServlet extends HttpServlet {

    public static long appid = 6713188;
    public static String displayType = "page";
    public static String redirectUrl = "http://188.166.194.200:8888/showFriends";
    public static String permissions = "friends,offline";
    public static String responseType = "code";
    public static String apiVersion = "5.85";

    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {

        String revoke = (String)httpServletRequest.getParameter("revoke");
        if(revoke == null){
            revoke="0";
        }else if (revoke.equals("1")){
            httpServletRequest.getSession().setAttribute("access_token", null);
        }

        if(httpServletRequest.getSession().getAttribute("access_token") == null) {
            StringBuilder address = new StringBuilder();

            address.append("https://oauth.vk.com/authorize?");

            address.append("client_id=");
            address.append(appid);
            address.append("&");

            address.append("display=");
            address.append(displayType);
            address.append("&");

            address.append("redirect_uri=");
            address.append(redirectUrl);
            address.append("&");

            address.append("scope=");
            address.append(permissions);
            address.append("&");

            address.append("response_type=");
            address.append(responseType);
            address.append("&");

            address.append("v=");
            address.append(apiVersion);
            address.append("&");

            address.append("revoke=");
            address.append(revoke);



            Util.wait(this.getServletConfig());
            httpServletResponse.sendRedirect(address.toString());
        }else{
            httpServletResponse.sendRedirect(redirectUrl);
        }
    }
}