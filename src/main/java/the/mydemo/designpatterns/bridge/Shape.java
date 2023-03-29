package the.mydemo.designpatterns.bridge;

/**
 * 形状抽象类
 * @author xulh
 * @version 1.0
 * @date 2023/3/2 11:03
 */
public abstract class Shape {

    protected IColor color;

    public Shape(IColor color){
        this.color=color;
    }

    public abstract void draw();
}
