package the.flash.algorithm;

import java.util.Arrays;

/**
 * Created by xulh on 2019/10/23.
 */
public class MergeSort {

    public static void main(String[] args) {

        int[] arr = {54,23,67,1,45,2,34,2,43,2,35,3};
        MergeSort sort = new MergeSort();
        sort.mergeSort(arr, 0, arr.length);

        System.out.println("排序后,arr="+ Arrays.toString(arr));
    }

    /**
     * 将
     * @param arr
     * @param left
     * @param right
     */
    public void mergeSort(int[] arr, int left, int right){
        if(left < right){
            int mid = (right - left)/2;
            mergeSort(arr,0, mid);
            mergeSort(arr, mid+1, right);
            merge(arr, left, mid, right);
        }
    }

    /**
     * 归并
     * @param arr
     * @param left
     * @param mid
     * @param right
     */
    public void merge(int[] arr, int left, int mid, int right){

        //申请一个与原数组大小相同的数组
        int[] temp = new int[right - left + 1];
        int i = left;
        int j = mid +1;
        int k = 0;

        //将a数组的每一个数和 b数组的每一个数进行比较,将排列好的值存到临时数组temp里
        while(i <= mid && j <= right){
            if(arr[i] < arr[j]){
                temp[k] = arr[i];
                i++;
            } else {
                temp[k] = arr[j];
                j++;
            }
            k++;
        }
        //将左边a数组剩余的数据填充到temp临时数组
        while (i <= mid){
            temp[k] = arr[i];
            k++;
            i++;
        }
        //将右边b数组剩余的数据填充到temp临时数组中
        while (j <= right){
            temp[k] = arr[j];
            k++;
            j++;
        }
        //将临时数组中的数据回填到正式数组中
        for (int t = 0; t < temp.length; t++){
            arr[left+t] = temp[t];
        }
    }
}
