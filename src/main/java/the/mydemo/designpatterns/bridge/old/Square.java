package the.mydemo.designpatterns.bridge.old;

/**
 * 正方形的形状实现
 * @author xulh
 * @version 1.0
 * @date 2023/3/2 10:50
 */
public class Square extends Shape{

    @Override
    public void draw() {
        System.out.println("正方形");
    }
}
