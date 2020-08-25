package com.kaikeba.homework.KKB_4_6.express.view;

import com.kaikeba.homework.KKB_4_6.express.bean.Express;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Views {
    private Scanner input = new Scanner(System.in);
    private static final int MENU_COMMAND_MAX_VALUE = 4;
    private static final int MENU_COMMAND_MIN_VALUE = 0;
    /**
     * 欢迎
     */
    public void welcome(){
        System.out.println("欢迎使用XXX快递管理系统");
    }

    /**
     * 再见
     */
    public void bye(){
        System.out.println("欢迎下次使用 ~");
    }

    /**
     * 选择身份的菜单
     * @return
     */
    public int menu(){
        System.out.println("请根据提示 ， 输入功能序号: ");
        System.out.println("1。快递员");
        System.out.println("2. 普通用户");
        System.out.println("0. 退出");

        String text = input.nextLine();
        int num = -1;
        try{
            num = Integer.parseInt(text);
        }catch (NumberFormatException e){

        }
        if(num<0 || num>2){
            System.out.println("输入有误,请重新输入");
            return menu();
        }
        return num;
    }

    /**
     * todo: 魔法值
     * 快递员菜单
     * @return num返回的为 0-4 的值
     */
    public  int cMenu(){
        System.out.println("请根据提示 , 输入功能序号:");
        System.out.println("1. 快递录入");
        System.out.println("2. 快递修改");
        System.out.println("3. 快递删除");
        System.out.println("4. 查看所有快递");
        System.out.println("0. 返回上级目录 ");

        String text = input.nextLine();
        int num = -1;
        try{
            num = Integer.parseInt(text);
        }catch (NumberFormatException e){

        }
        if(num < MENU_COMMAND_MIN_VALUE || num > MENU_COMMAND_MAX_VALUE){
            System.out.println("输入有误,请重新输入");
            return cMenu();
        }
        return num;
    }

    /**
     * 快递员录入快递
     * @return  包含了快递单号和快递公司的对象
     */
    public Express insert(){
        System.out.println("请根据提示，输入快递信息");
        System.out.println("请输入快递单号:");
        String number = input.nextLine();
        System.out.println("请输入快递公司:");
        String company = input.nextLine();
        Express e = new Express();
        e.setCompany(company);
        e.setNumber(number);
        return e;
    }

    /**
     * 提示用户输入快递单号
     * @return number : 用户输入的快递单号
     */
    public  String findExpressByNumber(){
        System.out.println("请根据提示，输入快递信息:");
        System.out.println("请输入要修改的快递单号:");
        String number = input.nextLine();
        return number;
    }

    /**
     * 显示快递信息
     * @param e
     */
    public void printExpress(Express e){
        System.out.println("快递信息如下:");
        System.out.println("快递公司" + e.getCompany() + ", 快递单号:"+e.getNumber()+",取件码:"+e.getCode());
    }

    public void printNull(){
        System.out.println("快递不存在 ，请检查进度输入.");
    }


    /**
     * 修改快递信息
     * @param e
     */
    public void update(Express e){
        System.out.println("请输入新的快递单号");
        String number = input.nextLine();
        System.out.println("请输入新的快递公司");
        String company = input.nextLine();
        e.setNumber(number);
        e.setCompany(company);
    }

    /**
     * 询问是否确认删除
     * @return 1表示确认 2表示取消操作
     */
    public  int delete(){
        System.out.println("是否确认删除?");
        System.out.println("1. 确认删除");
        System.out.println("2. 取消操作");
        String text = input.nextLine();
        int num = -1;
        try{
            num = Integer.parseInt(text);
        }catch (NumberFormatException e){

        }
        if(num<1 || num>2){
            System.out.println("输入有误,请重新输入");
            return delete();
        }
        return num;
    }

    /**
     * 将给定数组的快递信息 遍历显示
     * @param es
     */
    public void printAllExpressData(Map<Integer,Express> es){
        int count = 0;
        Set<Integer> esKeys = es.keySet();
        for (int esKey : esKeys){
            System.out.print("第" + (esKey / 10 + 1) + "排" + (esKey % 10 + 1) + "列快递,");
            printExpress(es.get(esKey));
            count++;
        }
        if(count == 0){
            System.out.println("暂无快递信息");
        }
    }

    /**
     * 用户菜单
     * @return
     */
    public int uMenu(){
        System.out.println("请根据提示，进行取件");
        System.out.println("请输入您的取件码:");
        String code = input.nextLine();
        int num = -1;
        try{
            num = Integer.parseInt(code);
        }catch (NumberFormatException e){

        }
        if(num<100000 || num>999999){
            System.out.println("输入有误,请重新输入");
            return uMenu();
        }
        return num;
    }

    public void expressExist(){
        System.out.println("此单号在快递柜已经存在 ， 请勿重新存储.");
    }

//    public void printCode(Express e){
//        System.out.println("快件的取件码为:"+e.getCode());
//    }
    public void success(){
        System.out.println("操作成功");
    }

    public void expressCabinetIsFull() {
        System.out.println("快递柜已满");
    }
}

