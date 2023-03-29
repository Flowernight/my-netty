package the.mydemo.designpatterns.bridge;

/**
 * @author xulh
 * @version 1.0
 * @date 2023/3/2 11:08
 */
public class Circle extends Shape{

    public Circle(IColor color) {
        super(color);
    }


    @Override
    public void draw() {
        color.getColor();
        System.out.println("圆形的");
    }
}
