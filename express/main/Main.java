package com.kaikeba.homework.KKB_4_6.express.main;

import com.kaikeba.homework.KKB_4_6.express.bean.Express;
import com.kaikeba.homework.KKB_4_6.express.dao.ExpressDao;
import com.kaikeba.homework.KKB_4_6.express.view.Views;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;


public class Main {
    /**
     * 初始化视图对象
     */
    private  static final Views VIEWS = new Views();
    /**
     * 初始化dao对象
     */
    private  static final ExpressDao DAO = new ExpressDao();
    /**
     * Socket
     */
    private static Socket sc = null;
    public static void main(String[] args) throws IOException {
        sc = new Socket("127.0.0.1",8081);
        DAO.setSocket(sc);
        //1.    欢迎
        VIEWS.welcome();
        //从文件中读取保存的数据
        System.out.println("客户端:20");
        DAO.sendCommand(1);
        System.out.println("客户端:22");
        DAO.receiveData();
        System.out.println("客户端:23");
        m:while(true) {
            //2.    弹出身份选择菜单
            int menu = VIEWS.menu();
            switch (menu) {
                case 0:
                    break m;
                case 1:
                    cClient();
                    break;
                case 2:
                    uClient();
                    break;
                default: break;
            }
        }
//        dao.writeAll();
        DAO.sendCommand(2);
        DAO.sendData();
        VIEWS.bye();
        sc.close();
    }

    private static void uClient() {
        //1.    取件码的获取
        int code = VIEWS.uMenu();
        //2.    根据取件码，取出快递
        Express e = DAO.findExpressByCode(code);
        if (e == null){
            VIEWS.printNull();
        }else  {
            VIEWS.success();
            VIEWS.printExpress(e);
            DAO.delete(e);
        }
    }
    private static void cClient() {
        while(true){
            int menu = VIEWS.cMenu();
            switch (menu){
                case 0:
                    return;

                case 1: {
                    //1.    提示输入快递信息
                    Express e = VIEWS.insert();
                    //2.    此快递是否已经存在
                    Express e2 = DAO.findExpressByNumber(e.getNumber());
                    //3.    存储快递
                    if (e2 == null){
                        //未存储过，存
                        DAO.add(e);
                        VIEWS.printExpress(e);
                    }else {
                        //单号在快递柜中已经存在
                        VIEWS.expressExist();
                    }
                }
                    break;

                //快递修改
                case 2:{
                    //1.    提示输入快递信息
                    String number = VIEWS.findExpressByNumber();
                    //2.    查找数据
                    Express e = DAO.findExpressByNumber(number);
                    Express e2 = e;
                    //3.    打印快递信息
                    if(e == null){
                        VIEWS.printNull();
                    }else {
                        VIEWS.printExpress(e);
                        //4.    提示修改
                        VIEWS.update(e2);
                        DAO.update(e,e2);
                        DAO.updateFile(e2);;
                        VIEWS.printExpress(e2);
                    }
                }
                    break;

                case 3:{
                    //删除
                    //1.    输入快递单号
                    String number = VIEWS.findExpressByNumber();
                    //2.    查找快递对象
                    Express e = DAO.findExpressByNumber(number);
                    if(e == null){
                        VIEWS.printNull();
                    }else {
                        VIEWS.printExpress(e);
                        int type = VIEWS.delete();
                        if(type == 1){
                         DAO.delete(e);
                            VIEWS.success();
                        }
                    }
                }
                    break;

                case 4:{
                    //查看所有
                    Map<Integer,Express> data = DAO.findAllExpressData();
                    VIEWS.printAllExpressData(data);
                }
                    break;

                default:break;
            }
        }
    }
}
