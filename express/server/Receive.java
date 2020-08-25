package com.kaikeba.homework.KKB_4_6.express.server;

import com.kaikeba.homework.KKB_4_3.KKB_4_3_1.bean.Express;
import com.kaikeba.homework.KKB_4_6.express.constant.Constant;

import java.io.*;

/**
 * todo : init() fw
 * @Author: 吃瓜
 * @Description:
 * @Date Created in 2020-08-10 13:59
 * @Modified By:
 */
public class Receive {
    private final String FILE_PATH = ".\\Serve\\saveData.txt";
    private File file = new File(FILE_PATH);
    private FileWriter fw = null;


    public synchronized void receive(InputStream is){
        String receiveExpressData = null;
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            //初始化fw
            init();

            //当发送结束时会发送"sendOver"为结束标志。
            //之前 while (!Constant.SEND_OVER.equals(receiveExpressData  = br.readLine())){
            receiveExpressData = br.readLine();
            while (!Constant.SEND_OVER.equals(receiveExpressData)){
                System.out.println("sendover : Receive: server:" + receiveExpressData);
                writeFile(receiveExpressData);
                receiveExpressData = br.readLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            close();
        }
    }

    private void writeFile(String str) throws IOException {
        //判断父路径是否存在
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        //文件内容格式 位置 快递号  快递公司 取件码
        //            58  415209  顺丰    123789
        fw.append(str + "\r\n");
    }

    private void init() throws IOException {
        fw = new FileWriter(file);
    }

    private void close(){
        try {
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
