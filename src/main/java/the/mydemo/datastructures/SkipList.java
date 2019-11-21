package the.mydemo.datastructures;

import java.util.Random;


class SkipListTest{
    public static void main(String[] args) {
        SkipList skipList = new SkipList();
        skipList.put(2, "a");
        skipList.put(3, "b");
        skipList.put(4, "t");
        skipList.put(10, "r");
        skipList.put(60, "c");
        skipList.put(10, "l");

        //遍历
        skipList.printl(skipList.head);

        //查找
        SkipListEntry findSkip = skipList.findEntry(10);
        System.out.println("查找到的元素" + findSkip);

    }
}
/**
 * Created by xulh on 2019/11/19.
 * 跳跃表数据结构
 */
public class SkipList {

    public SkipListEntry head;
    public SkipListEntry tail;

    public int n;
    public int h;

    public Random r;

    public SkipList(){
        SkipListEntry p1,p2;
        p1 = new SkipListEntry(Integer.MIN_VALUE, null);//-oo
        p2 = new SkipListEntry(Integer.MAX_VALUE, null);//+oo

        //将首尾节点相连
        p1.right = p2;
        p2.left = p1;

        head = p1;
        tail = p2;

        n = 0;
        h = 0;
        r = new Random();
    }

    //查找
    public SkipListEntry findEntry(Integer key){
        SkipListEntry p ;

        //从头开始找
        p = head;

        while (true){
//            System.out.println("p="+p.toString());
            //从左向右开始查找,直到右节点key值大于要查找的key的值
            while (p.right.key != Integer.MAX_VALUE && p.right.key <= key){
                p = p.right;
            }

            //如果有更低层的节点,则向低层移动
            if(p.down != null){
                p = p.down;
            } else {
                break;
            }
        }
        //返回p，这里key的值是小于等于传入key的值的.p.key <= key
        return p;
    }

    /**
     *
     * @param key
     * @return
     */
    public String get(Integer key){
        SkipListEntry p;
        p = findEntry(key);
        if (p.key.equals(key)){
            return p.value;
        } else {
            return null;
        }
    }

    /**
     * 添加元素
     * @param key
     * @param value
     * @return
     */
    public String put(Integer key, String value){
        SkipListEntry findEntry ,newEntry;
        int i = 0;

        //查找适合插入的位置
        findEntry = findEntry(key);
        //如果跳跃表中存在含有key值的节点,则进行value的修改操作即可完成
        if (findEntry.key.equals(key)){
            String oldValue = findEntry.value;
            findEntry.value = value;
            return oldValue;
        }

        //如果跳跃表中不存在含有key值的节点,则进行新增操作
        newEntry = new SkipListEntry(key, value);
        newEntry.left = findEntry;
        newEntry.right = findEntry.right;
        findEntry.right.left = newEntry;
        findEntry.right = newEntry;
        newEntry.setHigh(i);//设置层数
        //再用随机数决定是否要向更高level攀升
        while (r.nextInt() % 2 == 0){
            //如果新元素的级别已经达到跳跃表的最大高度,则新增空白层
            if(i >= h){
                addEmptyLevel();
            }

            //从p向左扫描含有高层节点的节点
            /*while (findEntry.up == null){
                findEntry = findEntry.left;
            }
            findEntry = findEntry.up;
             //新增和q指针指向的节点含有相同key值的节点对象
            //这里需要注意的是除底层节点之外的节点对象是不需要value值的
            SkipListEntry z = new SkipListEntry(key, null);
            z.left = findEntry;
            z.right = findEntry.right;
            findEntry.right.left = z;
            findEntry.right = z;
            z.down = findEntry;
            newEntry.up = z;
            newEntry = z;
            */
            //从新节点往左变查找上一个高度的左节点
            SkipListEntry left = newEntry.left;
            while (left.up == null){
                left = left.left;
            }
            left = left.up;

            //新增和newEntry的节点含有相同key值的节点对象
            //这里需要注意的是除底层节点之外的节点对象是不需要value值的
            SkipListEntry z = new SkipListEntry(key, null);
            z.left = left;
            z.right = left.right;
            left.right = z;
            z.right.left = z;
            z.down = newEntry;
            newEntry.up = z;
            newEntry = z;
            z.setHigh(i);
            i = i+1;
        }
        n = n+1;
        return null;
    }

    private void addEmptyLevel(){
        SkipListEntry p1,p2;
        p1 = new SkipListEntry(Integer.MIN_VALUE, null);
        p2 = new SkipListEntry(Integer.MAX_VALUE, null);

        p1.right = p2;
        p1.down = head;

        p2.left = p1;
        p2.down = tail;

        head.up = p1;
        tail.up = p2;

        head = p1;
        tail = p2;

        h = h+1;
    }

    /**
     * 移除跳表中的元素
     * @param key
     * @return
     */
    public String remove(Integer key){
        SkipListEntry p,q;
        p = findEntry(key);
        if(!p.key.equals(key)){
            return null;
        }

        String oldValue = p.value;
        while (p != null){
            q = p.up;
            p.left.right = p.right;
            p.right.left = p.left;
            p = q;
        }
        return oldValue;
    }

    /**
     * 遍历跳表
     * @param entry
     */
    public void printl(SkipListEntry entry){
        while (entry.down != null){
            entry = entry.down;
        }
        System.out.println(entry);
        if (entry != null && entry.right != null){
            printl(entry.right);
        }
    }

}

class SkipListEntry{

    public Integer key;
    public String value;
    public Integer high;//高度

    //links
    public SkipListEntry up;
    public SkipListEntry down;
    public SkipListEntry left;
    public SkipListEntry right;

    //special
    public static final String negInf = "-oo";
    public static final String posInf = "+oo";

    public SkipListEntry(Integer key, String value){
        this.key = key;
        this.value = value;
    }

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    @Override
    public String toString() {
        return "SkipListEntry{" +
                "key=" + key +
                ", value='" + value + '\'' +
                ", high=" + high +
                '}';
    }
}
