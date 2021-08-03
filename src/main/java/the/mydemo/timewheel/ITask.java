package the.mydemo.timewheel;

/**
 * Created by xulh on 2021/6/7.
 */
public abstract class ITask implements  Runnable{

    private Integer offset;

    Integer getOffset() {
        return offset;
    }

    void setOffset(Integer offset) {
        this.offset = offset;
    }
}
