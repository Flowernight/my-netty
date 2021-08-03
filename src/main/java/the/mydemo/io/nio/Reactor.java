package the.mydemo.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by xulh on 2021/7/28.
 * reactor模型
 */
public class Reactor implements Runnable {

    public static void main(String[] args) {
        Reactor reactor = new Reactor(8080);
        reactor.run();
    }

    Selector selector;
    ServerSocketChannel serverSocket;

    Reactor(int port){
        try {
            selector = Selector.open();
            serverSocket = ServerSocketChannel.open();
            serverSocket.socket().bind(new InetSocketAddress(port));
            serverSocket.configureBlocking(false);//设置成非阻塞io
            SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);//第一步接收accept事件
            sk.attach(new Acceptor());//设置回调对象
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            while (!Thread.interrupted()){
                selector.select();
                Set<SelectionKey> selected = selector.selectedKeys();
                Iterator<SelectionKey> it = selected.iterator();
                while (it.hasNext()){
                    dispatch(it.next());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void dispatch(SelectionKey k){
        Runnable r = (Runnable) k.attachment();//调用之前注册的callback对象
        if (r != null){
            r.run();
        }
    }

    //获取连接handler
    class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel c = serverSocket.accept();
                if(c != null){
                    new Handler(selector,c);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * 业务处理类
 */
final class Handler implements Runnable {

    final SocketChannel socket;
    final SelectionKey sk;
    ByteBuffer input = ByteBuffer.allocate(1024);
    ByteBuffer output = ByteBuffer.allocate(1024);
    static final int READING = 0,SENDING=1;
    int state = READING;

    Handler(Selector sel,SocketChannel c) throws IOException {
        socket = c;
        c.configureBlocking(false);
        sk = socket.register(sel,0);
        sk.attach(this);//将handler当作callback对象
        sk.interestOps(SelectionKey.OP_READ);//第二步，接收read事件
        sel.wakeup();
    }

    boolean inputIscomplete(){
        return false;
    }

    boolean outputIscomplete(){
        return false;
    }

    void process(){}

    @Override
    public void run() {
        try {
            if (state == READING)
                read();
            if (state == SENDING)
                send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void read() throws IOException {
        socket.read(input);
        if (inputIscomplete()){
            process();//业务处理
            state = SENDING;
            sk.interestOps(SelectionKey.OP_WRITE);//第三步，接收write事件
        }
    }

    void send() throws IOException {
        socket.write(output);
        if(outputIscomplete()){
            sk.cancel();//写完返回值，关闭select key
        }
    }
}
