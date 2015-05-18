package com.trangpig.until;

/**
 * Created by TrangPig on 04/19/2015.
 */
public class MyUri {

    public static String IP = "192.168.137.1";
    public static final String URL_WEBSOCKET = "ws://%s:8080/tuyensinhchat/chat?id=%s";
    public static final String LOGIN = "http://%s:8080/tuyensinh/login?username=%s&password=%s";
    public static final String CONVERSATION = "http://%s:8080/tuyensinh/getconversation";
    public static final String URL_UP_IMAGE = "http://%s:8080/tuyensinh/image";
    public static final String URL_DOWN_IMAGE = "http://%s:8080/tuyensinh/image?filename=%s";
    public static final String URL_GET_ACCOUNT = "http://%s:8080/tuyensinh/getacc/%s";
    public static final String URL_GET_LIST_ADD_FRIEND = "http://%s:8080/tuyensinh/getlistaddfriend?idacc=%s";

}
