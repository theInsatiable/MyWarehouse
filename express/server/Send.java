package com.kaikeba.homework.KKB_4_6.express.server;


import java.io.*;

/**
 * @Author: 吃瓜
 * @Description: 向客户端发送保存到数据
 * @Date Created in 2020-08-10 13:59
 * @Modified By:
 */
public class Send {
    private PrintStream ps = null;
    private static final String FILE_PATH = ".\\Serve\\saveData.txt";
    public synchronized void readFile(PrintStream ps){
        this.ps = ps;
        File file = new File(FILE_PATH);
        System.out.println("服务器:Send:19行");
        try {

            //检查文件父路径是否存在
            if (! file.getParentFile().exists()){
                System.out.println("创建Serve文件夹及保存文件");
                file.getParentFile().mkdir();
                file.createNewFile();
                sendData("sendOver");
                return;
            }

            //检查文件是否存在
            else if (! file.exists()) {
                System.out.println("创建保存文件");
                file.createNewFile();
                sendData("sendOver");
                return;
            }

            //检查文件是否为空
            else if (file.length() == 0) {
                System.out.println("Send : 35");
                sendData("sendOver");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Send : 42");

        try {
            if (file.length() != 0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String line;
                while ((line = reader.readLine()) != null) {
                    sendData(line);
                    line = "";
                }
                sendData("sendOver");
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("服务器Send:结束");
    }

    private void sendData(String str){
        ps.println(str);
    }
}
