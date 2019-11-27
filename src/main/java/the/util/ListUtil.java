package the.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xulh on 2019/11/27.
 */
public class ListUtil {

    /**
     * 把list切割成小list
     * @param list
     * @param batch
     * @return
     */
    public static <T> List<List<T>> subListForNum(List<T> list, int batch) {
        //非空判断
        if(list == null || batch < 1) {
            return null;
        }

        List<List<T>> listArr = new ArrayList<>();
        int size = list.size();
        int beginIndex = 0;
        int endIndex = 0;
        for(int i=0; i < size/batch+1; i++){
            beginIndex = i*batch;
            endIndex = (i+1)*batch;
            if(beginIndex == size)
                continue;
            if(endIndex > size)
                endIndex = size;
            List<T> subList = list.subList(beginIndex, endIndex);
            listArr.add(subList);
        }
        return listArr;
    }
}
