package the.mydemo.timewheel;

/**
 * Created by xulh on 2021/6/7.
 */
public interface IWheel {

    /**
     * 添加任务
     * @param task 任务对象
     * @return taskId 任务编号
     */
    String addTask(ITask task,Integer delayedTime);

    /**
     *
     * @param taskId
     * @return 任务是否删除成功
     */
    boolean removeTask(String taskId);

    /**
     * 更新一个延时任务
     * @param taskId 任务Id
     * @param delayedTime 延时时间
     * @return 新的任务ID
     */
    String delayedTask(String taskId,Integer delayedTime);
}
