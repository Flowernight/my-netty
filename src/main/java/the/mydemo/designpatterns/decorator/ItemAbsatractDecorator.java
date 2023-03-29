package the.mydemo.designpatterns.decorator;

/**
 * 定义抽象装饰者：创建传参(抽象构件)构造方法，以便给具体构件增加功能
 * @author xulh
 * @version 1.0
 * @date 2023/2/24 17:37
 */
public abstract class ItemAbsatractDecorator implements  ItemComponent{

    protected ItemComponent itemComponent;

    public ItemAbsatractDecorator(ItemComponent myItem) {
        this.itemComponent = myItem;
    }

    public double checkoutPrice() {
        return this.itemComponent.checkoutPrice();
    }
}
