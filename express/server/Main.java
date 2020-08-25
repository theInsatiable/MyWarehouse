package com.kaikeba.homework.KKB_4_6.express.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @Author: 吃瓜
 * @Description:
 * @Date Created in 2020-08-10 13:59
 * @Modified By:
 */
public class Main {
    public static ServerSocket serverSocket = null;
//todo:线程池
    public static void main(String[] args) throws IOException {
        serverSocket = new ServerSocket(8081);
        ExecutorService threadPoolExecutor = new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        while (true){
            System.out.println("服务器:20行");
            Socket socket = serverSocket.accept();
            threadPoolExecutor.execute(() -> {
                System.out.println("一个客户端连接");
                m:while (true){
                    try {
                        System.out.println("服务器:28行");
                        // 如果socket连接断开，即客户端意外中断，那么 socket.getInputStream();代码将死循环报错
                        if (socket.isClosed()){
                            System.out.println("客户端异常结束了");
                            break m;
                        }
                        System.out.println("服务器:33行");
                        InputStream is = socket.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        System.out.println("服务器:31");
                        // 如上述的死循环错误 下面这行也有可能发生。但只发生过一次无法检查错误。
                        // 也想不到更加可靠的办法。
                        String command = br.readLine();

                        System.out.println("服务器:42");

                        // 如果接收到"1" 则说明 客户端启动，将服务端保存的数据发送给客户端。
                        // 如果接收到"2" 则说明 客户端结束，客户端将数据发送给服务端保存。
                        switch (command){
                            case "1" :
                                PrintStream ps = new PrintStream(socket.getOutputStream());
                                new Send().readFile(ps);
                                break ;
                            case "2" :
                                new Receive().receive(is);
                                break m;
                            default : break m;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }

    }
}
