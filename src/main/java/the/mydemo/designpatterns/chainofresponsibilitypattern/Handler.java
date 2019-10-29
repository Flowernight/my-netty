package the.mydemo.designpatterns.chainofresponsibilitypattern;

/**
 * Created by xulh on 2019/10/28.
 * 抽象处理者角色类
 */
public abstract class Handler {

    //持有下一个请求的对象
    protected Handler successor = null;

    //获取值
    public Handler getSuccessor(){
        return successor;
    }

    public void setSuccessor(Handler successor){
        this.successor = successor;
    }

    //处理费用的申请
    public abstract String handlerRequest(String user, double fee);
}
