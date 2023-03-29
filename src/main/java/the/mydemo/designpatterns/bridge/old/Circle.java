package the.mydemo.designpatterns.bridge.old;

/**
 * 圆形的形状实现
 * @author xulh
 * @version 1.0
 * @date 2023/3/2 10:51
 */
public class Circle extends Shape{

    @Override
    public void draw() {
        System.out.println("圆形");
    }
}
