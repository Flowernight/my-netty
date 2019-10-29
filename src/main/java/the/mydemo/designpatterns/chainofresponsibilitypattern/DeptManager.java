package the.mydemo.designpatterns.chainofresponsibilitypattern;

/**
 * Created by xulh on 2019/10/28.
 */
public class DeptManager extends Handler {
    @Override
    public String handlerRequest(String user, double fee) {

        String str = "";
        if(fee < 1000){
            str = String.format("%s,申请成功,申请的费用为:%s元,审核者为部门经理",user,fee+"");
        } else if (getSuccessor() != null){
            return getSuccessor().handlerRequest(user, fee);
        }
        return str;
    }
}
