package the.mydemo.designpatterns.bridge;

import the.mydemo.designpatterns.bridge.old.WhiteCircle;

/**
 * 场景：
 * 用ppt画图的时候可以画圆形、正方形等，可以给图形填充白色、红色等。
 * 如果是继承则有2*2=4个类
 * 如果用桥接的话就会减少类的数量
 *
 *
 * @author xulh
 * @version 1.0
 * @date 2023/3/2 10:56
 */
public class Client {

    public static void main(String[] args) {
        //老的继承的模式
        WhiteCircle wc = new WhiteCircle();
        wc.draw();

        //桥接模式
        WhiteColor white = new WhiteColor();
        Circle circle = new Circle(white);
        circle.draw();
    }
}
