package the.mydemo.timewheel;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xulh on 2021/6/7.
 */
public class TimerWheel implements IWheel {

    private final Map<Long, ITask>[] wheel; // 轮子
    private final int wheelSize; // 轮子大小
    private final AtomicInteger currentOffset; // 当前偏移量
    private final AtomicLong nextId = new AtomicLong(1); // 任务编号;
    private final ExecutorService workers = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @SuppressWarnings("all")
    public TimerWheel(int interval, int wheelSize, int firstOffset) {
        this.wheelSize = wheelSize;
        this.wheel = new HashMap[wheelSize];
        currentOffset = new AtomicInteger(firstOffset);
        for (int i = 0; i < wheelSize; i++) {
            this.wheel[i] = new HashMap<Long, ITask>();
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                int offsetValue = currentOffset.getAndIncrement();
                int pointer = offsetValue % wheelSize; // 指针
                Map<Long, ITask> slot = wheel[pointer];
                if (!slot.isEmpty()) {
                    slot.entrySet().removeIf(entry -> {
                        ITask task = entry.getValue();
                        workers.submit(task);
                        return task.getOffset() == offsetValue;
                    });
                }
            }
        }, firstOffset, interval*1000);
    }

    @Override
    public String addTask(ITask task, Integer delayedTime) {
        long taskId = nextId.getAndIncrement();
        int offset = delayedTime + currentOffset.get();
        task.setOffset(offset);
        int slotIndex = offset % wheelSize;
        Map<Long, ITask> slot = wheel[slotIndex];
        slot.put(taskId, task);
        return "task-" + Integer.toString(slotIndex, 32) + "-" + Long.toString(taskId, 32);

    }

    @Override
    public boolean removeTask(String taskId) {
        if (taskId != null && !taskId.trim().isEmpty()) {
            String[] arr = taskId.split("-");
            if (arr.length == 3) {
                int slotIndex = Integer.valueOf(arr[1], 32);
                Map<Long, ITask> slot = wheel[slotIndex];
                if (!slot.isEmpty()) {
                    long key = Long.valueOf(arr[2], 32);
                    slot.remove(key);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String delayedTask(String taskId, Integer delayedTime) {
        if (taskId != null && !taskId.trim().isEmpty()) {
            String[] arr = taskId.split("-");
            if (arr.length == 3) {
                int slotIndex = Integer.valueOf(arr[1], 32);
                Map<Long, ITask> slot = wheel[slotIndex];
                if (!slot.isEmpty()) {
                    long key = Long.valueOf(arr[2], 32);
                    ITask task = slot.remove(key);
                    if (task != null) {
                        return addTask(task, delayedTime);
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        IWheel iWheel = new TimerWheel(1,60,5);
        String taskId=iWheel.addTask(new ITask() {
            @Override
            public void run() {
                System.out.println("测试任务执行");
            }
        },20);
        System.out.println(taskId);
    }
}
