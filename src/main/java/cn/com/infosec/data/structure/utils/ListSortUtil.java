/**
 * ListSortUtil
 * <p>
 * 1.0
 * <p>
 * 2023/1/4 9:59
 */

package cn.com.infosec.data.structure.utils;

import cn.com.infosec.data.structure.bean.SelectNode;

import java.lang.reflect.Method;
import java.util.*;

public class ListSortUtil {

    public static <T> void sort(List<T> list, final String method, final String order) {
        Collections.sort(list, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                int ret = 0;
                try {
                    Method method1 = o1.getClass().getMethod(method, null);
                    Method method2 = o2.getClass().getMethod(method, null);
                    //倒序
                    if ((Objects.nonNull(order) && order.trim().length() > 0) && "desc".equalsIgnoreCase(order)) {
                        ret = method2.invoke(o2, null).toString().compareTo(method1.invoke(o1, null).toString());
                    } else {
                        ret = method1.invoke(o1, null).toString().compareTo(method2.invoke(o2, null).toString());
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ret;
            }
        });
    }


    public static void main(String[] args) {
        SelectNode sn1 = new SelectNode();
        sn1.setNodePath("/1/0/1/0/1/0/1/0/1");
        SelectNode sn2 = new SelectNode();
        sn2.setNodePath("/1/0/1/0/1/1");
        SelectNode sn3 = new SelectNode();
        sn3.setNodePath("/1/0/2/2");
        List<SelectNode> selectNodes = new ArrayList<>();
        selectNodes.add(sn1);
        selectNodes.add(sn2);
        selectNodes.add(sn3);



        sort(selectNodes,"getNodePath","desc");

        for(SelectNode sn:selectNodes) {
            System.out.println(sn.getNodePath());
        }

        System.err.println("/1/0/1/0/1/0/1/0/1".compareTo("/1/0/1/0/1/1"));
        System.err.println("/1/0/1/0/1/1".compareTo("/1/0/1/0/1/0/1/0/1"));
        System.err.println("/1/0/2/2".compareTo("/1/0/1/0/1/0/1/0/1"));
    }


}
