package the.mydemo.designpatterns.bridge.old;

/**
 * 白色的圆形
 * @author xulh
 * @version 1.0
 * @date 2023/3/2 10:55
 */
public class WhiteCircle extends Circle{

    @Override
    public void draw() {
        System.out.println("白色的");
        super.draw();
    }
}
