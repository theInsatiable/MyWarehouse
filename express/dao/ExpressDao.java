package com.kaikeba.homework.KKB_4_6.express.dao;

import com.kaikeba.homework.KKB_4_6.express.bean.Express;
import com.kaikeba.homework.KKB_4_6.express.constant.Constant;
import com.kaikeba.homework.KKB_4_6.express.view.Views;

import java.io.*;
import java.net.Socket;
import java.util.*;

/**
* @Description
* @Author 吃瓜
* @Date Created in  2020.8.24
**/
public class ExpressDao {
    //    private Express[][] data = new Express[10][];

    /**
     *  键是快递在柜中位置，值是 快递对象。
     */
    private Map<Integer, Express> data = new HashMap<>();
    /**
     * 当前存储的快递数
     */
    private Random random = new Random();
    private Socket socket = null;
    final static private int EXPRESS_CABINET_MAX = 100;
    /**
     * 用于存储快递
     *
     * @param e
     * @return
     */
    public boolean add(Express e) {
        // 快递柜满了
        if (data.size() == EXPRESS_CABINET_MAX) {
            new Views().expressCabinetIsFull();
            return false;
        }
        //1.    随机生成2个0-99的下标
        int x = -1;
        while (true) {
            x = random.nextInt(100);
            if (!data.containsKey(x)) {
                //此位置无快递
                break;
            }
        }
        //2.    取件码
        int code = randomCode();
        e.setCode(code);
        data.put(x, e);
        return true;
    }

    /**
     * 生成随机取件码
     *
     * @return
     */
    private int randomCode() {
        while (true) {
            int code = random.nextInt(900000) + 100000;
            Express e = findExpressByCode(code);
            if (e == null) {
                return code;
            }
        }
    }

    public Express findExpressByNumber(String number) {
        Express e = new Express();
        e.setNumber(number);
        Collection<Express> dataValues = data.values();
        for (Express dataValue : dataValues) {
            if (e.equals(dataValue)) {
                return dataValue;
            }
        }
        return null;
    }

    /**
     * 根据取件码查询快递
     *
     * @param code 要查询的取件码
     * @return 查询的结果，查询失败是返回null
     */
    public Express findExpressByCode(int code) {
        Collection<Express> dataValues = data.values();
        for (Express dataValue : dataValues) {
            if (dataValue.getCode() == code) {
                return dataValue;
            }
        }
        return null;
    }

    /**
     * 多余的操作，为了mvc更圆润
     *
     * @param oldExpress
     * @param newExpress
     */
    public void update(Express oldExpress, Express newExpress) {
        delete(oldExpress);
        add(newExpress);

    }

    public void delete(Express e) {
        Set<Integer> dataKeys = data.keySet();
        for (int dataKey : dataKeys) {
            if (e.equals(data.get(dataKey))) {
                data.replace(dataKey, data.get(dataKey), null);
                data.remove(dataKey);
                deleteFile(dataKey);
                break;
            }
        }
    }

    public Map<Integer, Express> findAllExpressData() {
        return data;
    }

    /**
     * todo : f
     * @Description 从文件中读文取件全部内容
     **/
    public void readAllExpressDataFromFile() {
        File file = new File(".\\Save");
        //文件不存在的处理
        if (!file.exists()) {
            file.mkdir();
            return;
        }

        BufferedReader br = null;
        File[] listFiles = file.listFiles();
        List<String> expressValue = new ArrayList<>();
        try {
            for (File oneFileInList : listFiles) {
                br = new BufferedReader(new FileReader(oneFileInList));
                expressValue.add(oneFileInList.getName());
                for (int i = 0; i < 3; i++) {
                    expressValue.add(br.readLine());
                }
            }
        } catch (FileNotFoundException fileNotFound) {
            fileNotFound.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            try {
                if (br != null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Express e = new Express();
        for (int n = 0; n < expressValue.size(); n += 4) {
            e.setCode(Integer.parseInt(expressValue.get(n + 1)));
            e.setNumber(expressValue.get(n + 2));
            e.setCompany(expressValue.get(n + 3));
            data.put(Integer.parseInt(expressValue.get(n)), e);
        }

    }

    /**
    * @Description 将信息全部保存到文件中
    **/
    public void writeAllExpressDataToFile() {
        Set<Integer> keys = data.keySet();
        try {
            for (int key : keys) {
                updateFile(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
    * @Description 更新文件内容。
    * @param key 快递在集合中的位置
    **/
    public void updateFile(int key) throws IOException {
        File file = new File(".\\Save\\" + key);
        //文件夹不存在
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file);
        Express e = data.get(key);
        fw.append(e.getCode() + "\n")
                .append(e.getNumber() + "\n")
                .append(e.getCompany());
        fw.close();
    }

    public void updateFile(Express express) {
        Set<Integer> dataKeys = data.keySet();
        int key = 0;
        for (int key1 : dataKeys) {
            if (express.equals(data.get(key1))) {
                key = key1;
                break;
            }
        }
        File file = new File(".\\Save\\" + key);
        try {
            if (file.getParentFile().exists()) {

            } else {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            Express e = data.get(key);
            fw.append(e.getCode() + "\n")
                    .append(e.getNumber() + "\n")
                    .append(e.getCompany());
            fw.close();
        }catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    public boolean deleteFile(int key) {
        File file = new File(".\\Save");
        File[] listFiles = file.listFiles();
        for (File deleteFile : listFiles) {
            if (String.valueOf(key).equals(deleteFile.getName())) {
                return deleteFile.delete();
            }
        }
        return false;
    }

    /**
     * 给服务端发送程序运行后的数据
     * todo:空格 改逗号
     * todo:拼完再发
     */
    public void sendData(){
        StringBuilder sendExpressData = new StringBuilder();
        Set<Integer> keys = data.keySet();
        try{
            OutputStream os = socket.getOutputStream();
            PrintStream ps = new PrintStream(os);

            for (int key : keys) {
                sendExpressData
                        .append(key + ",")
                        .append(data.get(key).getCode() + ",")
                        .append(data.get(key).getCompany() + ",")
                        .append(data.get(key).getNumber() + "&");
//                sendExpressData = new StringBuilder();
            }
            ps.println(sendExpressData);
            ps.println(Constant.SEND_OVER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收服务端向客户端发来的数据
     *
     */
    public void receiveData(){
        System.out.println("receiveData:263");
        String receiveAllExpressData = null;
        Express express = new Express();

        try{
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            System.out.println("receiveData:287");

            //在发送结束后会发送"sendOver"作为结束标志。
            //之前 while (!Constant.SEND_OVER.equals(receiveExpressData = br.readLine()))
            //更改为while前加一行和while块最后一行赋值。感觉不如之前可读性高
            receiveAllExpressData = br.readLine();
            while (!Constant.SEND_OVER.equals(receiveAllExpressData)) {
                System.out.println("receiveData:291");

                System.out.println("receiveAllExpressData:" + receiveAllExpressData);

                //先按段分，即拆分出每个快递信息
                String[] allExpressData = receiveAllExpressData.split("&");
                for (String oneExpressData : allExpressData) {

                    //expressData中为快递的各属性信息
                    String[] expressData = oneExpressData.split(",");

                    //此处for循环用于调试查看信息
                    for (String str : expressData) {
                        System.out.println("value:" + str);
                    }

                    //此处应有判断空指针异常的程序,懒得写了，因为可能存在某个值为空
                    express.setCode(Integer.parseInt(expressData[1]));
                    express.setCompany(expressData[2]);
                    express.setNumber(expressData[3]);
                    data.put(Integer.parseInt(expressData[0]), express);
                    express = new Express();
                }
                receiveAllExpressData = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("receiveData:323");
    }

    /***
    * @Description
    * @param command = 1:代表客户端程序启动，向服务端要数据
     *               = 2:代表程序结束，将要向服务端保存数据
     *               如果要设置其他命令需要在serve等类中进行相应更改
    **/
    public void sendCommand(int command){
        OutputStream os = null;
        PrintStream  ps = null;
        try {
            os = socket.getOutputStream();
            ps = new PrintStream(os);
            ps.println(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
