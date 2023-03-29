package the.mydemo.designpatterns.decorator;

/**
 *
 * 定义具体装饰者A：增加店铺折扣八折
 * @author xulh
 * @version 1.0
 * @date 2023/2/24 17:39
 */
public class ShopDiscountDecorator extends ItemAbsatractDecorator {

    public ShopDiscountDecorator(ItemComponent myItem) {
        super(myItem);
    }

    @Override
    public double checkoutPrice() {
        return 0.8 * super.checkoutPrice();
    }
}
