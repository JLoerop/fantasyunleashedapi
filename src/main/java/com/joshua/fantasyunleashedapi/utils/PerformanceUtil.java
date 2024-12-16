package com.joshua.fantasyunleashedapi.utils;

import java.time.Instant;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.time.Duration;

//utility used for logging which records the amount of time and level of the service that is ran
public class PerformanceUtil {
    private static final Logger logger = Logger.getLogger(PerformanceUtil.class.getName());
    public static Instant start() {return Instant.now();}
    public static void stop(Instant startTime){
        Duration timeElapsed = Duration.between(startTime, Instant.now());
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        logger.log(Level.INFO, stackTrace[2].getMethodName(), new Object[]{timeElapsed.toMillis()});
    }
}
