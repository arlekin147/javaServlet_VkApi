<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.webim.UserInfo"%>

<head>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

    <style>
        a {
            color: inherit;
        }
        a:visited{
            color: inherit;
        }
    </style>
</head>
<body>
<h3 class="center-align">Five Random Friends</h3>
<%
String name = (String)request.getAttribute("name");
out.print("<h4>"+name+"</h4>");
%>

<div class="row" style="display: flex; justify-content: center;">
<% List<UserInfo> usersInfo = (ArrayList<UserInfo>)request.getAttribute("usersInfo");

    for(UserInfo userInfo : usersInfo)
    {
        out.print("<div class=\"col s12 m5\">");
        out.print("<div class=\"card\">");
        out.print("<a href = //vk.com/id" + userInfo.getId() + ">");
        out.print("<div class=\"card-image\">");
        out.print("<img src=" + userInfo.getPhotoUrl() +"\">");
        out.print("</div>");
        out.print("<a href = //vk.com/id" + userInfo.getId() + ">");
        out.print("<span class=\"card-title\">" + userInfo.getFirstName() +" "+ userInfo.getLastName() + "</span>");
        out.print("</div></div>");
    }

%>
</div>

<a class="waves-effect waves-light btn blue accent-4" href="/redirectToAuth?revoke=1">Logout
<i class="material-icons right">directions_run</i>
</a>
</body>