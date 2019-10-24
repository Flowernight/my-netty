package the.flash.algorithm;

/**
 * 8皇后问题
 * 在8*8的棋盘上摆放8个皇后,要求是每个皇后不能与其他的任一个
 * 在同一行，同一列，同一对角线
 * Created by xulh on 2019/10/17.
 */
public class Queen8 {

    int max = 8;
    //一个数组代表一种解法,数组的下标表示行,每个值代表列
    int[] arr = new int[8];

    static int count = 0;

    public static void main(String[] args) {
        Queen8 queen = new Queen8();
        queen.check(0);
        System.out.printf("总共有%d组答案",count);
    }

    public void check(int n){
        //如果8个都摆好则退出
        if(n == 8){
            print();
            return;
        }
        //摆放棋子
        for (int i=0; i < max; i++){
            arr[n] = i;
            //如果该棋子不与前面的冲突则递归计算后面的棋子位置
            if(judge(n)){
                check(n+1);
            }
        }
    }

    /**
     * 判断第n个是否和前面几个皇后冲突
     * @param n
     */
    public boolean judge(int n){
       for (int i=0;i<n;i++){
           //判断不在同一列，不在对角线上
           if (arr[i]==arr[n] || Math.abs(n-i)==Math.abs(arr[n]-arr[i])){
               return false;
           }
       }
       return true;
    }

    public void print(){
        for (int i = 0 ; i<arr.length ;i++){
            System.out.print(arr[i]+1+" ");
        }
        count++;
        System.out.println();
    }
}
