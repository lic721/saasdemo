package com.saas.demo.common.util;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Marker;
import org.springframework.util.StringUtils;

import java.util.Iterator;

/**
 * 添加日志定位ID
 */
public class LogFilterConverter extends ClassicConverter {
    static final String filterStr = "<%s> <%s>";

    @Override
    public String convert(ILoggingEvent iLoggingEvent) {
        Marker marker = iLoggingEvent.getMarker();
        String filter1 = "";
        String filter2 = "";
        if (marker != null && "filter".equals(marker.getName())) {
            Iterator<Marker> markerIterator = marker.iterator();
            if (markerIterator.hasNext()) {
                filter1 = markerIterator.next().getName();
                if (markerIterator.hasNext()) {
                    filter2 = markerIterator.next().getName();
                }
            }
        }
        if (StringUtils.isEmpty(filter2)) {
            filter2 = iLoggingEvent.getMDCPropertyMap().getOrDefault("gLogId", "");
        }

        return String.format(filterStr, filter1, filter2);
    }
}
