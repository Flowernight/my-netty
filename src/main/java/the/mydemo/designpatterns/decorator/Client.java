package the.mydemo.designpatterns.decorator;

/**
 * @author xulh
 * @version 1.0
 * @date 2023/2/24 17:41
 */
public class Client {

    public static void main(String[] args) {
        ItemComponent item = new ConcreteItemCompoment();
        System.out.println("宝贝原价：" + item.checkoutPrice() + " 元");
        item = new ShopDiscountDecorator(item);
        System.out.println("使用店铺折扣后需支付：" + item.checkoutPrice() + " 元");
        item = new FullReductionDecorator(item);
        System.out.println("使用满200减20后需支付：" + item.checkoutPrice() + " 元");

        //输出
        //宝贝原价：200.0 元
        //使用店铺折扣后需支付：160.0 元
        //使用满200减20后需支付：140.0 元
    }
}
