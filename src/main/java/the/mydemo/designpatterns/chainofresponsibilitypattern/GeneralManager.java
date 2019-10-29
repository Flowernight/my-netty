package the.mydemo.designpatterns.chainofresponsibilitypattern;

/**
 * Created by xulh on 2019/10/28.
 */
public class GeneralManager extends Handler {
    @Override
    public String handlerRequest(String user, double fee) {
        String str = "";
        if (fee  < 5000){
            str  = String.format("%s,审核通过,申请金额为：%s,审核人为generalmanager",user,fee+"");
        } else if(getSuccessor() != null){
            return getSuccessor().handlerRequest(user, fee);
        }
        return str;
    }
}
