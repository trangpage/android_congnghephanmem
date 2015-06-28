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
    public static final String URL_GET_MAKE_FRIEND = "http://%s:8080/tuyensinh/makefriend?idacc=%s&idfri=%s";
    public static final String URL_GET_UN_MAKE_FRIEND = "http://%s:8080/tuyensinh/unmakefriend?idacc=%s&idfri=%s";
    public static final String URL_GET_COMMIT_MAKE_FRIEND = "http://%s:8080/tuyensinh/commitmakefriend?idacc=%s&idfri=%s";
    public static final String URL_GET_ALL_GROUPTOPIC = "http://%s:8080/tuyensinh/getallgrouptopic";
    public static final String URL_GET_GROUPTOPIC = "http://%s:8080/tuyensinh/getgrouptopic/%s";
    public static final String URL_GET_TOPIC = "http://%s:8080/tuyensinh/gettopic/%s";
    public static final String URL_CREATE_POST = "http://%s:8080/tuyensinh/createpost";
    public static final String URL_CREATE_TOPIC = "http://%s:8080/tuyensinh/createtopic";
    public static final String URL_SIGN_UP = "http://%s:8080/tuyensinh/signup";
    public static final String URL_UPDATE_LIKE = "http://%s:8080/tuyensinh/updatelike";
    public static final String URL_FIND_STUDENT = "http://%s:8080/tuyensinh/findstudent?value=%s";
    public static final String URL_FIND_STUDENT_BY_SBD = "http://%s:8080/tuyensinh/findbysbd?sbd=%s";


}
