package the.mydemo.algorithm;

import java.util.Arrays;

/**
 * 快速排序,比较稳定的排序算法,时间复杂度为O（logn）
 * Created by xulh on 2019/10/23.
 * 思路: 选取最左边的数作为基准 X,数组有最开始的索引 start和最后的索引 end
 * 先把end位的值和X比较,把start向后找；如果end位的数比X小并且start比X大,则start和end位的数交换位置;
 *
 */
public class QuickSort {

    public static void main(String[] args) {
        //{3,7,1,8,2,4,2}
        int[] arr = {1, 2, 2, 3, 8, 4, 7};//{34,54,23,1,9,2,8,3,7,4,3,0}
        QuickSort sort = new QuickSort();
        sort.quickSort(arr, 0, 6);

        System.out.println("排序后的数组arr:"+ Arrays.toString(arr));
    }

    public void quickSort(int[] arr, int start, int end){

        if(start > end){
            return;
        }

        //基准数据
        int X = arr[start];
        int left = start;
        int right = end;
        while (left != right){

            //如果右索引没有找到比基准数小的则索引往左移
            while (left < right && arr[right] >= X){
                right--;
            }
            //如果左索引没有找到比基准数大的则索引往右移
            while (left < right && arr[left] <= X){
                left ++ ;
            }

            //说明已经找到了左右交换的数
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
        }

        //两个索引重合，则将基准数和改位的数交换
        arr[start] = arr[left];
        arr[left] = X;

        //分别对两边的数据进行递归处理
        quickSort(arr,start,left-1);
        quickSort(arr, left+1, end);
    }

}
