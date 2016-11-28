package edu.mit.lib.rest.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>Title: MIT Library Practice</p>
 * <p>Description: edu.mit.lib.rest.utils.X</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 9/12/2016
 */
public class X extends Thread implements Runnable {

    public void run() {
        System.out.print("This is the run()\n");
    }

    @SafeVarargs
    private static <K, V> void filter(List<Map<K, V>> source, K... keys) {
        if (source != null && source.size() > 0) {
            if (keys != null && keys.length > 0) {
                Set<String> id = new HashSet<>();
                StringBuilder values = new StringBuilder();
                for (Iterator<Map<K, V>> iterator = source.iterator(); iterator.hasNext(); ) {
                    Map<K, V> item = iterator.next();
                    values.delete(0, values.length());
                    for (K key : keys) {
                        values.append("<%");
                        if (item.get(key) != null) {
                            values.append(item.get(key));
                        }
                        values.append("%>");
                    }
                    if (!id.contains(values.toString())) {
                        id.add(values.toString());
                    } else {
                        iterator.remove();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Thread app = new Thread(new X());
        app.start();

        Map<String, Object> item1 = new HashMap<>(3);
        item1.put("SNo", 1);
        item1.put("SOURCE_UNID", 101);
        item1.put("SOURCE_TYPE", "Job");

        Map<String, Object> item2 = new HashMap<>(3);
        item2.put("SNo", 2);
        item2.put("SOURCE_UNID", 102);
        item2.put("SOURCE_TYPE", "Payment");

        Map<String, Object> item3 = new HashMap<>(3);
        item3.put("SNo", 1);
        item3.put("SOURCE_UNID", 101);
        item3.put("SOURCE_TYPE", "Job");

        List<Map<String, Object>> source = new ArrayList<>();
        source.add(item1);
        source.add(item2);
        source.add(item3);
        filter(source, "SNo", "SOURCE_UNID", "SOURCE_TYPE");

        source.forEach(System.out::println);
    }
}
