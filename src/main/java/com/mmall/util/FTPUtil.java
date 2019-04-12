package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);
    private static String ftpIp = PropertitesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertitesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertitesUtil.getProperty("ftp.pass");

    public FTPUtil(String ip,int port,String user,String pwd){
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException{
        FTPUtil ftpUtil = new FTPUtil(ftpIp,21,ftpUser,ftpPass);
        logger.info("开始连接FTP服务器");
        boolean result = ftpUtil.uploadFile("img",fileList);
        logger.info("开始连接FTP服务器，结束长传，上传结果:{}");
        return result;
    }

    private boolean uploadFile(String remotePath,List<File> fileList) throws IOException{
        boolean uploaded = true;
        FileInputStream fileInputStream = null;
        //连接FTP服务器
        if (connectServer(this.ip,this.port,this.user,this.pwd)){
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("utf-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                for (File fileItem : fileList){
                    fileInputStream = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fileInputStream);
                }
            }catch (IOException e){
                logger.error("上传文件异常",e);
                e.printStackTrace();
            }finally {
                fileInputStream.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }
    private boolean connectServer(String ip,int port,String user,String pwd){

        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            ftpClient.login(user,pwd);
            isSuccess = ftpClient.login(user,pwd);

        } catch (IOException e){
            logger.error("连接FTP服务器异常",e);
        }
        return isSuccess;
    }

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPart() {
        return port;
    }

    public void setPart(int part) {
        this.port = part;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
