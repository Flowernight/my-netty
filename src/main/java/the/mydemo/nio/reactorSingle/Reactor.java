package the.mydemo.nio.reactorSingle;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by xulh on 2021/7/26.
 */
public class Reactor implements Runnable {

    private Selector selector;

    private Reactor(Selector selector,ServerSocketChannel serverSocket) throws ClosedChannelException {
        this.selector = selector;
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
//        sk.attach()
    }
    @Override
    public void run() {

    }

    public static class Acceptor implements Runnable {

        @Override
        public void run() {
//            selector.selectedKeys();
        }
    }
}

class Acceptor implements Runnable {



    @Override
    public void run() {

    }
}
