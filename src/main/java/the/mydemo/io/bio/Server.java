package the.mydemo.io.bio;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xulh on 2021/7/28.
 */
public class Server implements Runnable{

    @Override
    public void run() {
        try {
            //绑定端口
            ServerSocket ss = new ServerSocket(8080);
            while (!Thread.interrupted()){
                //获取已经完成3次握手的socket连接
                Handler handler = new Handler(ss.accept());
                //启用线程处理业务流程
                new Thread(handler).start();
            }
        } catch (Exception e ){

        }
    }

    public static class Handler implements Runnable {

        final Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                //read
                byte[] bs = new byte[1024];
                //此处的read是阻塞的
                int read = socket.getInputStream().read(bs);
                //process
                //write
                socket.getOutputStream().write(bs);
            } catch (Exception e){

            }
        }
    }
}
