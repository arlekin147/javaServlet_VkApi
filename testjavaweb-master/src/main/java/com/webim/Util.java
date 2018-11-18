package com.webim;

import javax.servlet.ServletConfig;
import java.util.Date;

public class Util {
    public static void wait(ServletConfig config){
        Date timeOfLastrequest = (Date) (config.getServletContext().getAttribute("timeOfLastRequest"));
        if(timeOfLastrequest != null) {
            if (timeOfLastrequest.getTime() - new Date().getTime() < 500) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        config.getServletContext().setAttribute("timeOfLastRequest", new Date());
    }
}
