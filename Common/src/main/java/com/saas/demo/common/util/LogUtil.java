package com.saas.demo.common.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.MarkerFactory;

import java.util.UUID;

public class LogUtil {
    private static String SPLIT_LEFT = "<";
    private static String SPLIT_RIGHT = ">";

    private static final String classPath = "classPath";
    private static final String className = "className";
    private static final String methodName = "methodName";
    private static final String saas = "saas";

    public static void info(String message) {
        JSONObject jsonObject = parseCallInfo();
        Logger logger = getLogger(jsonObject);
        String logMsg = buildLogInfo(saas, jsonObject.get(className).toString(), jsonObject.get(methodName).toString(), message, null, getLogId());
        logger.info(logMsg);
    }

    public static void info(String module, String category, String subCategory, String message) {
        JSONObject jsonObject = parseCallInfo();
        Logger logger = getLogger(jsonObject);
        String logMsg = buildLogInfo(saas, jsonObject.get(className).toString(), jsonObject.get(methodName).toString(), message, null, getLogId());
        logger.info(logMsg);
    }


    public static void info(String module, String category, String subCategory, String msg, String filter1,
                            Class<?> clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        String logMsg = buildLogInfo(module, category, subCategory, msg, filter1, getLogId());
        logger.info(logMsg);
    }

    public static String getLogId() {
        String logId = gLogIdThreadLocal.get();
        if (logId == null) {
            logId = UUID.randomUUID().toString().replace("-", "");
            gLogIdThreadLocal.set(logId);
        }
        return logId;
    }

    private static InheritableThreadLocal<String> gLogIdThreadLocal = new InheritableThreadLocal<String>();


    public static org.slf4j.Marker getFilter(String filter1, String filter2) {
        org.slf4j.Marker marker = MarkerFactory.getDetachedMarker("filter");
        if (filter1 != null) {
            marker.add(MarkerFactory.getDetachedMarker(filter1));
        }
        if (filter2 != null) {
            marker.add(MarkerFactory.getDetachedMarker(filter2));
        }
        return marker;
    }

    public static org.slf4j.Marker getFilter(String filter1) {

        org.slf4j.Marker marker = MarkerFactory.getDetachedMarker("filter");
        if (filter1 != null) {
            marker.add(MarkerFactory.getDetachedMarker(filter1));
        }
        return marker;
    }

    public static void setGLogId(String gLogId) {
        if (!org.springframework.util.StringUtils.isEmpty(gLogId)) {
            gLogIdThreadLocal.set(gLogId);
            MDC.put("gLogId", gLogId);
        }
    }

    public static void warn(String module, String category, String subCategory, String msg, String filter1, Class<?> clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        String logMsg = buildLogInfo(module, category, subCategory, msg, filter1, getLogId());
        logger.warn(logMsg);

    }

    public static void warn(String message) {
        JSONObject jsonObject = parseCallInfo();
        Logger logger = getLogger(jsonObject);
        String logMsg = buildLogInfo(saas, jsonObject.get(className).toString(), jsonObject.get(methodName).toString(), message, null, getLogId());
        logger.warn(logMsg);
    }


    public static void error(String module, String category, String subCategory, String msg, String filter1, Throwable t,
                             Class<?> clazz) {
        Logger logger = LoggerFactory.getLogger(clazz);
        String logMsg = buildLogInfo(module, category, subCategory, msg, filter1, getLogId());
        logger.error(logMsg, t);
    }

    public static void error(String message, Throwable t) {
        JSONObject jsonObject = parseCallInfo();
        Logger logger = getLogger(jsonObject);
        String logMsg = buildLogInfo(saas, jsonObject.get(className).toString(), jsonObject.get(methodName).toString(), message, null, getLogId());
        logger.error(logMsg, t);
    }

    public static void error(String module, String category, String subCategory, String message, Throwable t) {
        JSONObject jsonObject = parseCallInfo();
        Logger logger = getLogger(jsonObject);
        String logMsg = buildLogInfo(saas, jsonObject.get(className).toString(), jsonObject.get(methodName).toString(), message, null, getLogId());
        logger.error(logMsg, t);
    }


    /**
     * 格式化日志
     *
     * @param module
     * @param category
     * @param subCategory
     * @param msg
     * @param filter1
     * @param filter2
     * @return
     */
    private static String buildLogInfo(String module, String category, String subCategory, String msg, String filter1,
                                       String filter2) {
        StringBuffer sb = new StringBuffer();
        sb.append(SPLIT_LEFT).append(module).append(SPLIT_RIGHT);
        sb.append(SPLIT_LEFT).append(category).append(SPLIT_RIGHT);
        sb.append(SPLIT_LEFT).append(subCategory).append(SPLIT_RIGHT);
        sb.append(SPLIT_LEFT).append(filter1).append(SPLIT_RIGHT);
        sb.append(SPLIT_LEFT).append(filter2).append(SPLIT_RIGHT);
        sb.append(SPLIT_LEFT).append(msg).append(SPLIT_RIGHT);
        return sb.toString();
    }


    /**
     * 解析使用log的类和方法
     *
     * @return
     */
    private static JSONObject parseCallInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(classPath, "");
        jsonObject.put(className, "");
        jsonObject.put(methodName, "");
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace != null && stackTrace.length > 0) {
            StackTraceElement log = stackTrace[1];
            for (int i = 1; i < stackTrace.length; i++) {
                StackTraceElement e = stackTrace[i];
                if (!e.getClassName().equals(log.getClassName())) {
                    String className = e.getClassName();
                    if (className.indexOf(".") != -1) {
                        String[] split = className.split("\\.");
                        jsonObject.put(LogUtil.className, split[split.length - 1]);
                    }
                    jsonObject.put(classPath, e.getClassName());
                    jsonObject.put(methodName, e.getMethodName());
                    break;
                }
            }
        }
        return jsonObject;
    }

    private static Logger getLogger(JSONObject jsonObject) {
        String classPath = jsonObject.get(LogUtil.classPath).toString();
        Class<?> clazz = LogUtil.class;
        if (StringUtils.isNotEmpty(classPath)) {
            try {
                clazz = Class.forName(classPath);
            } catch (ClassNotFoundException e) {
                clazz = LogUtil.class;
            }
        }
        Logger logger = LoggerFactory.getLogger(clazz);
        return logger;
    }
}

