package com.app.common.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 *
 */
public class Exceptions {
	/**
     * 将CheckedException转换成UncheckedException.
     * @param e
     * @return
     */
    public static RuntimeException unchecked(Exception e) {
        if(e instanceof RuntimeException) {
            return (RuntimeException) e;
        }else {
            return new RuntimeException(e);
        }
    }

    /**
     * 将错误的堆栈转化为String
     * @param e
     * @return
     */
    public static String getStackTraceToString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
