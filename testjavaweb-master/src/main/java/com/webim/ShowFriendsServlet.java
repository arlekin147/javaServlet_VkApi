package com.webim;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ShowFriendsServlet extends HttpServlet {

    public static long appid = 6713188;
    public static String secret = "c5KWmftKcXcEndndr45F";
    public static String order = "random";
    public static int count = 5;
    public static String apiVersion = "5.85";
    public static String fields = "first_name,last_name,photo_max";
    public static String redirectUrl = "http://188.166.194.200:8888/showFriends";
    

    private static String sendRequest(String urlstr) throws IOException{
        final StringBuilder result = new StringBuilder();
        URL url = new URL(urlstr);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            int codeResponse = urlConnection.getResponseCode();
            if(codeResponse != 200) throw new BadResponseException(codeResponse);
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");  // Put entire content to next token string, Converts utf8 to 16, Handles buffering for different width packets

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                throw new IOException();
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
            httpServletResponse.setContentType("text/html; charset=UTF-8"); //TODO mb move to listOfFriends


            String token = (String)httpServletRequest.getSession().getAttribute("access_token");
            if(token == null) {
                String code = httpServletRequest.getParameter("code");
                if(code == null){
                    httpServletResponse.sendRedirect("/redirectToAuth");
                    return;
                }

                StringBuilder adress = new StringBuilder();
                adress.append("https://oauth.vk.com/access_token?");

                adress.append("client_id=");
                adress.append(appid);
                adress.append("&");

                adress.append("client_secret=");
                adress.append(secret);
                adress.append("&");

                adress.append("redirect_uri=");
                adress.append(redirectUrl);
                adress.append("&");

                adress.append("code=");
                adress.append(code);
                adress.append("&");

                Util.wait(this.getServletConfig());
                String tokenInfoStr = null;
                this.getServletConfig().getServletContext().setAttribute("timeOfLastRequest", new Date());
                try {
                    tokenInfoStr = sendRequest(adress.toString());
                }catch(IOException e){
                    httpServletResponse.sendRedirect("/redirectToAuth?revoke=1");
                    return;
                }

                JSONObject tokenInfo = new JSONObject(tokenInfoStr);
                token = tokenInfo.getString("access_token");
                if(token == null){ //still?!! OMG
                    httpServletResponse.sendRedirect("/redirectToAuth?revoke=1");
                }
                httpServletRequest.getSession().setAttribute("access_token", token);
            }


            StringBuilder address = new StringBuilder();

            address.append("https://api.vk.com/method/friends.get?");

            address.append("order=");
            address.append(order);
            address.append("&");

            address.append("count=");
            address.append(count);
            address.append("&");

            address.append("fields=");
            address.append(fields);
            address.append("&");

            address.append("access_token=");
            address.append(httpServletRequest.getSession().getAttribute("access_token"));
            address.append("&");

            address.append("v=");
            address.append(apiVersion);
            address.append("&");

            Util.wait(this.getServletConfig());
            String vkResponceStr = null;
            try {
                vkResponceStr = sendRequest(address.toString());
            }catch(IOException e){ //TODO handle exception
                httpServletResponse.getWriter().println(e.getMessage());
                return;
            }

            //TODO make it all null-safe
            JSONObject vkResponse = new JSONObject(vkResponceStr);
            JSONArray items = vkResponse.getJSONObject("response").getJSONArray("items");

            ArrayList<UserInfo> friends = new ArrayList();
            for (int i = 0; i < items.length(); i++) {
                JSONObject friend = items.getJSONObject(i);
                friends.add(
                        new UserInfo(
                                friend.getString("first_name"),
                                friend.getString("last_name"),
                                new URL(friend.getString("photo_max")),
                                friend.getInt("id")
                        )
                );
            }
            httpServletRequest.setAttribute("usersInfo", friends);


            Util.wait(this.getServletConfig());
            vkResponceStr = null;
            try {
                vkResponceStr = sendRequest("https://api.vk.com/method/users.get?v="+
                        apiVersion+"&access_token="+token);
            }catch(IOException e){ //TODO handle exception
                httpServletResponse.getWriter().println(e.getMessage());
                return;
            }
            System.out.println(vkResponceStr);
            JSONObject json = new JSONObject(vkResponceStr).getJSONArray("response").getJSONObject(0); //TODO null safe

            httpServletRequest.setAttribute("name", json.getString("first_name")+" "+json.getString("last_name"));

            try {
                httpServletRequest.getRequestDispatcher("listOfFriends.jsp").forward(httpServletRequest, httpServletResponse);
            } catch (ServletException e) { //TODO handle exception
                httpServletResponse.getWriter().println(e.getMessage());
                return;
            }
    }
}