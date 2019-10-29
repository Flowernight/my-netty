package the.mydemo.designpatterns.chainofresponsibilitypattern;

/**
 * Created by xulh on 2019/10/28.
 * 责任链模式是一种对象的行为模式。在责任链模式里，很多对象由每一个对象对其下一家对象的引用而连接起来形成一条链。
 * 请求在这个链上传递，直到链上的某一个对象决定处理该请求。发出请求的客户端并不知道链上的哪一个对象最终处理这个
 * 请求,这使得系统可以在不影响客户端的情况下动态的重新组织和分配责任。
 * tomcat容器的filter链就是一个典型的责任链模式
 * 它是启动的时候就将所有的拦截器都注册到一个数组中ApplicationFilterConfig,
 * 当请求进来时ApplicationFilterChain会依次调用数组中的每个过滤器，起到过滤请求的目的
 */
public class Client {

    public static void main(String[] args) {
        Handler h3 = new ProjectManager();
        Handler h2 = new DeptManager();
        Handler h1 = new GeneralManager();
        h3.setSuccessor(h2);
        h2.setSuccessor(h1);

        String str1 = h3.handlerRequest("zs", 2000);
        System.out.println(str1);

        String str2 = h3.handlerRequest("ls", 50000);
        System.out.println(str2);
    }
}
