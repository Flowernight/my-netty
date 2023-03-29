package the.mydemo.designpatterns.decorator;

/**
 * 定义具体装饰者B：增加满200减20功能，此处忽略判断逻辑
 * @author xulh
 * @version 1.0
 * @date 2023/2/24 17:41
 */
public class FullReductionDecorator extends ItemAbsatractDecorator {

    public FullReductionDecorator(ItemComponent myItem) {
        super(myItem);
    }

    @Override
    public double checkoutPrice() {
        return super.checkoutPrice() - 20;
    }
}
