package com.aeonbank.library.common;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MdcUtil {

    private static final String TRACE_ID = "traceId";

    private static final HashFunction murmur3_32_fixed = Hashing.murmur3_32_fixed();

    public static void setTraceId() {
        MDC.put(TRACE_ID, genTraceId());
    }

    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static void clearTraceId() {
        MDC.remove(TRACE_ID);
    }

    public static String genTraceId() {
        return murmur3_32_fixed.hashString(UUID.randomUUID().toString(), StandardCharsets.UTF_8).toString();
    }

}
