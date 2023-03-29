package the.mydemo.designpatterns.decorator;

/**
 * // 定义具体构件：具体商品
 * @author xulh
 * @version 1.0
 * @date 2023/2/24 17:37
 */
public class ConcreteItemCompoment implements ItemComponent{

    // 原价
    @Override
    public double checkoutPrice() {
        return 200.0;
    }
}
