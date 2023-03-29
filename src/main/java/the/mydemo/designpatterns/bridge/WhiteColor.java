package the.mydemo.designpatterns.bridge;

/**
 * @author xulh
 * @version 1.0
 * @date 2023/3/2 11:07
 */
public class WhiteColor implements IColor{

    @Override
    public void getColor() {
        System.out.println("白色的");
    }
}
