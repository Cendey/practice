package edu.lab.mit.cell;



import org.testng.log4testng.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * <p>Project: MIT Project</p>
 * <p>File: edu.lab.mit.cell.ReflectUtils</p>
 * <p>Copyright: Copyright @2015 The MIT Lab. All Rights Reserved.</p>
 * <p>Company: MIT Education Organization</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 8/8/2015
 */
public class ReflectUtils {

    private static final Logger logger = Logger.getLogger(ReflectUtils.class);

    public static <T> void flushParams(Map<String, Object> params, T t) {
        if (params == null || t == null)
            return;

        Class<?> clazz = t.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields = clazz.getDeclaredFields();

                for (Field field : fields) {
                    String name = field.getName();
                    Object value;

                    if (logger.isDebugEnabled())
                        logger.debug(ReflectUtils.class + "method flushParams attribute name:" + name + "  ");


                    Method method =
                        t.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                    value = method.invoke(t);

                    if (logger.isDebugEnabled())
                        logger.debug(ReflectUtils.class + "attribute value:" + value);

                    if (value != null)
                        params.put(name, value);
                }
            } catch (Exception e) {
                logger.error(e.getCause().getMessage(), e);
            }
        }
    }

    public static <T> void flushObject(T t, Map<String, Object> params) {
        if (params == null || t == null)
            return;

        Class<?> clazz = t.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                Field[] fields = clazz.getDeclaredFields();

                for (Field field : fields) {
                    String name = field.getName();

                    if (logger.isDebugEnabled())
                        logger.debug(ReflectUtils.class + "method flushObject attribute name:" + name + "  ");

                    Object value = params.get(name);
                    if (value != null && !"".equals(value)) {
                        field.setAccessible(true);
                        field.set(t, value);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getCause().getMessage(), e);
            }
        }
    }
}
