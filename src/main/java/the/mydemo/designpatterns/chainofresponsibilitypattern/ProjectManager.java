package the.mydemo.designpatterns.chainofresponsibilitypattern;

/**
 * Created by xulh on 2019/10/28.
 *
 * 具体处理者角色,项目经理
 */
public class ProjectManager extends Handler {

    @Override
    public String handlerRequest(String user, double fee) {

        String str = "";
        if(fee < 500){
            str = String.format("%s,申请成功,申请的费用为%s",user,fee+"");
        } else if (getSuccessor() != null) {
            return getSuccessor().handlerRequest(user,fee);
        }
        return str;
    }
}
