package the.mydemo.timewheel;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xulh on 2021/6/8.
 */
public class TimerWheelBendan {

    public static void main(String[] args) throws InterruptedException {
        TimerWheelBendan timeWheelService = new TimerWheelBendan(3);


        timeWheelService.schedule(()->{
            /*for(int i=0;i<100;i++){
                final int a=i;
                timeWheelService.schedule(()-> System.out.println("^^^^^^buff-"+a),100,80);
            }*/

            System.out.println("------"+1+"-"+ new Date());
        },3000,2000);

        Thread.sleep(3000);

        timeWheelService.schedule(()->{
            System.out.println("-------"+2+"-"+new Date());
        },2000,2000);
       /* timeWheelService.schedule(()->{
            for(int i=0;i<100;i++){
                final int a=i;
                timeWheelService.schedule(()-> System.out.println("=====>debuff-"+a),100,100);
            }
        },100,0);
        timeWheelService.schedule(()-> System.out.println(new Date()),10,1000);*/
    }

    private MultiTimeWheel timeWheel=new MultiTimeWheel();
    private TimeWheelThread timeWheelThread=new TimeWheelThread();


    //每轮的时间轮长度
    private static final int TICK=10;
    private static final int wheelIndexMillisecondLength=1000/TICK;
    private static final int wheelIndexSecondLength=60;
    private static final int wheelIndexMinuteLength=60;
    private static final int wheelIndexHourLength=24;
    private static final int wheelIndexDayLength=365;

    //每一轮对应的所有ticks
    private static final long wheelMillisecondAllTicks=1L;
    //1s  10格
    private static final long wheelSecondAllTicks=wheelMillisecondAllTicks*wheelIndexMillisecondLength;
    //1min  600
    private static final long wheelMinuteAllTicks=wheelSecondAllTicks*wheelIndexSecondLength;
    //1h
    private static final long wheelHourAllTicks=wheelMinuteAllTicks*wheelIndexMinuteLength;
    /**1day 1天毫秒数/10   24*60*60*100 */
    private static final long wheelDayAllTicks=wheelHourAllTicks*wheelIndexHourLength;


    //每一轮当前的索引，可以精确获取时间
    private AtomicInteger wheelIndexMillisecond=new AtomicInteger(0);
    private AtomicInteger wheelIndexSecond=new AtomicInteger(0);
    private AtomicInteger wheelIndexMinute=new AtomicInteger(0);
    private AtomicInteger wheelIndexHour=new AtomicInteger(0);
    private AtomicInteger wheelIndexDay=new AtomicInteger(0);


    //实际存储
    private volatile Vector[] wheelMillisecond=new Vector[wheelIndexMillisecondLength];
    private volatile Vector[] wheelSecond=new Vector[wheelIndexSecondLength];
    private volatile Vector[] wheelMinute=new Vector[wheelIndexMinuteLength];
    private volatile Vector[] wheelHour=new Vector[wheelIndexHourLength];
    private volatile Vector[] wheelDay =new Vector[wheelIndexDayLength];


    public void schedule(Runnable runnable,long delay,long period){
        if(period<TICK && period>0) throw new RuntimeException("不能使得间隔周期小于时间片TICK："+TICK+"  ms,间隔周期可以为0ms");
        synchronized(this){
            TimeWheelTask timeWheelTask = new TimeWheelTask(delay, period, runnable);
            schedule(timeWheelTask);
        }
    }


    public void schedule(TimeWheelTask timeWheelTask){
        //delay 加上当前相对于具体时间单位的余数。
        //当前是0分59s时加入了1分1秒后任务，会导致在1分1秒时候执行，因此如果延迟本身大于当前的一轮周期，则用延迟加上当前时间与本轮毫秒值的余数
        //00:00:59 + 61 = 00:02:00,可知，需要先加上本轮余数
        timeWheelTask.delay=timeWheelTask.delay+timeWheel.getWheelNowTime(timeWheelTask.delay);
        System.out.println("初始延迟-"+timeWheelTask.delay);
        //时间轮中加入任务
        timeWheel.addTaskToWheel(timeWheelTask.delay,timeWheelTask);
    }



    //真正执行定时任务的线程池
    private ThreadPoolExecutor threadPoolExecutor;

    public TimerWheelBendan(int coreSize){
        this.threadPoolExecutor=new ThreadPoolExecutor(coreSize,coreSize,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(10000));
        timeWheelThread.start();
    }



    //轮子

    class MultiTimeWheel{

        /**
         * 增加  one-tick，可能会触发每层轮,时间轮的升级
         */
        public void incrTick() {

            //先把毫秒盘刻度+1.如果返回true，则说明不升级
            if(incIndex(TimeUnit.MILLISECONDS)){
                return;
            }

            if(incIndex(TimeUnit.SECONDS)){
                return;
            }

            if(incIndex(TimeUnit.MINUTES)){
                return;
            }

            if(incIndex(TimeUnit.HOURS)){
                return;
            }

            incIndex(TimeUnit.DAYS);

        }

        //增加一个tick，处理因为升级导致的新事件添加
        private boolean incIndex(TimeUnit timeUnit){
            long allTicksNext;//一个刻度对应多少个tick
            Vector[] vectorsNext;
            AtomicInteger index;//当前轮的指针
            AtomicInteger indexNext;//上层轮的指针
            int wheelLength;//本层轮的总刻度
            int wheelLengthNext;//上层轮的总刻度

            switch(timeUnit){
                case DAYS:
                    allTicksNext=0;
                    vectorsNext=null;
                    index=wheelIndexDay;
                    indexNext=null;
                    wheelLength=wheelIndexDayLength;
                    wheelLengthNext=0;
                    break;

                case HOURS:
                    allTicksNext=wheelDayAllTicks;
                    vectorsNext=wheelDay;
                    index=wheelIndexHour;
                    indexNext=wheelIndexDay;
                    wheelLength=wheelIndexHourLength;
                    wheelLengthNext=wheelIndexDayLength;
                    break;
                case MINUTES:
                    allTicksNext=wheelHourAllTicks;
                    vectorsNext=wheelHour;
                    index=wheelIndexMinute;
                    indexNext=wheelIndexHour;
                    wheelLength=wheelIndexMinuteLength;
                    wheelLengthNext=wheelIndexHourLength;
                    break;
                case SECONDS:
                    allTicksNext=wheelMinuteAllTicks;
                    vectorsNext=wheelMinute;
                    index=wheelIndexSecond;
                    indexNext=wheelIndexMinute;
                    wheelLength=wheelIndexSecondLength;
                    wheelLengthNext=wheelIndexMinuteLength;
                    break;
                case MILLISECONDS:
                    allTicksNext=wheelSecondAllTicks;
                    vectorsNext=wheelSecond;
                    index=wheelIndexMillisecond;
                    indexNext=wheelIndexSecond;
                    wheelLength=wheelIndexMillisecondLength;
                    wheelLengthNext=wheelIndexSecondLength;
                    break;
                default:
                    throw new RuntimeException("Timeunit 参数错误");
            }

            index.getAndIncrement();//当前轮的指针加1
            if(index.get()<wheelLength){//若加完1后的值小于当前轮的长度，则不做上层轮升级
                return true;
            }
            index.set(index.get()%wheelLength);//计算当前轮的指针。
            //如果是天数，因为当处理hours时候已经处理过天了，所以直接返回。
            if(timeUnit.equals(TimeUnit.DAYS)){
                return true;
            }

            //获取下一个时间轮的任务，并添加
            List<TimeWheelTask> taskList = vectorsNext[(indexNext.get() + 1) % wheelLengthNext];

            if(null!=taskList){
                for(TimeWheelTask task:taskList){
                    System.out.println("测试1="+task.delay+"-"+allTicksNext+"-"+task.delay%allTicksNext);
                    addTaskToWheel(task.delay%(allTicksNext),task);
                }
                taskList.clear();
            }
            return false;
        }

        public List<TimeWheelTask> getTaskList() {
            return wheelMillisecond[wheelIndexMillisecond.get()];
        }


        //加入时间轮，判断是否需要升级
        void addTaskToWheel(long delay,TimeWheelTask task){
            if(delay>=wheelIndexDayLength*wheelDayAllTicks*TICK){
                throw new RuntimeException("不能超过一年");
            }
            if(addTaskToWheel(delay,task,TimeUnit.DAYS)){
                return;
            }
            if(addTaskToWheel(delay,task,TimeUnit.HOURS)){
                return;
            }
            if(addTaskToWheel(delay,task,TimeUnit.MINUTES)){
                return;
            }
            if(addTaskToWheel(delay,task,TimeUnit.SECONDS)){
                return;
            }
            addTaskToWheel(delay,task,TimeUnit.MILLISECONDS);

        }


        //添加任务到时间轮，
        private boolean addTaskToWheel(long delay, TimeWheelTask timeWheelTask, TimeUnit timeUnit){
            long allTicks;
            Vector[] vectors;
            AtomicInteger index;
            int wheelLength;
            switch (timeUnit){
                case DAYS:
                    allTicks=wheelDayAllTicks;
                    vectors= wheelDay;
                    index=wheelIndexDay;
                    wheelLength=wheelIndexDayLength;
                    break;
                case HOURS:
                    allTicks=wheelHourAllTicks;
                    vectors=wheelHour;
                    index=wheelIndexHour;
                    wheelLength=wheelIndexHourLength;
                    break;
                case MINUTES:
                    allTicks=wheelMinuteAllTicks;
                    vectors=wheelMinute;
                    index=wheelIndexMinute;
                    wheelLength=wheelIndexMinuteLength;
                    break;
                case SECONDS:
                    allTicks=wheelSecondAllTicks;
                    vectors=wheelSecond;
                    index=wheelIndexSecond;
                    wheelLength=wheelIndexSecondLength;
                    break;
                case MILLISECONDS:
                    allTicks=wheelMillisecondAllTicks;
                    vectors=wheelMillisecond;
                    index=wheelIndexMillisecond;
                    wheelLength=wheelIndexMillisecondLength;
                    break;
                default:
                    throw new RuntimeException("timeUnit 参数错误");
            }

//            System.out.println("测试2-"+delay+"-"+index+"-"+allTicks+"-"+wheelLength);
            //添加到当前的索引。判断该延迟任务属于哪个级别
            if(0!=delay/(allTicks*TICK) || timeUnit.equals(TimeUnit.MILLISECONDS)){
                int indexNew=(index.get()+(int)(delay/(allTicks*TICK)))%wheelLength;
                if(null==vectors[indexNew]){
                    vectors[indexNew]=new Vector();
                }
                vectors[indexNew].add(timeWheelTask);
                return true;
            }
            return false;

        }


        //准确获取当前需要添加的准确时间轮
        long getWheelNowTime(long delay){
            //当前时间 毫秒 从任务启动到当前的时间，时间轮的时间
            long timeFromWheelStart=(wheelIndexDay.get()*wheelDayAllTicks+wheelIndexHour.get()*wheelHourAllTicks+wheelIndexMinute.get()*wheelMinuteAllTicks
                    +wheelIndexSecond.get()*wheelSecondAllTicks+wheelIndexMillisecond.get()*wheelMillisecondAllTicks)*TICK;
            System.out.println("当前时间-"+timeFromWheelStart);
            //从大到小开始处理，是否大于1天
            if(0!=delay/(wheelDayAllTicks*TICK)){
                return timeFromWheelStart%(wheelDayAllTicks*TICK);
            }

            //大于1小时
            if(0!=delay/(wheelHourAllTicks*TICK)){
                return timeFromWheelStart%(wheelHourAllTicks*TICK);
            }

            //大于1分钟
            if(0!=delay/(wheelMinuteAllTicks*TICK)){
                return timeFromWheelStart%(wheelMinuteAllTicks*TICK);
            }

            //大于1秒
            if(0!=delay/(wheelSecondAllTicks*TICK)){
                return timeFromWheelStart%(wheelSecondAllTicks*TICK);
            }

            return 0;
        }


    }


    /**
     * 时间轮的task
     */
    class TimeWheelTask implements Runnable{
        private long delay;
        //-1 为手动取消、0为1次定时、大于0表示正常调度间隔
        private long period;
        private Runnable runnable;

        public void setDelay(long delay) {
            this.delay = delay;
        }

        TimeWheelTask(long delay, long period, Runnable runnable){
            this.delay=delay;
            this.period=period;
            this.runnable=runnable;
        }

        /**
         * 判断是否是周期性的调度任务
         * @return
         */
        public boolean isPeriodSchedule(){
            return period>0;
        }

        @Override
        public void run() {
            if(this.period==-1){
                return;
            }
            runnable.run();
        }

        /**
         * 手动消除调度
         */
        public void cancel(){
            this.period=-1;
        }

    }


    //时间轮 main线程
    class TimeWheelThread extends Thread{

        public TimeWheelThread(){
            super("TimeWheel_main");
        }


        public TimeWheelThread(String thread_name){
            super(thread_name);
        }


        @Override
        public void run() {
            try {
                mainLoop();
            }catch (Exception e){
                System.out.println(e);
            }finally {
                System.out.println(111);
            }

        }

        private void mainLoop() {

            while (true){
                //运行任务
                runTask(timeWheel.getTaskList());//.先运行当前刻度上的任务
                //时间增加  one-tick
                timeWheel.incrTick();//再对当前刻度进行+1

                //休眠
                try {
                    TimeUnit.MILLISECONDS.sleep(TICK);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

            }

        }

        private void runTask(List<TimeWheelTask> taskList) {
            if(taskList==null || taskList.size()==0) return;
            for(TimeWheelTask timeWheelTask:taskList){
                threadPoolExecutor.execute(timeWheelTask);
                if(timeWheelTask.isPeriodSchedule()){
                    timeWheelTask.setDelay(timeWheelTask.period);
                    schedule(timeWheelTask);
                }
            }
            taskList.clear();


        }
    }

}
