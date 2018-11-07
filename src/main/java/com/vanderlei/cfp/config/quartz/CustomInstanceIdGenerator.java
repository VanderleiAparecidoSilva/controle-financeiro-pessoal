package com.vanderlei.cfp.config.quartz;

import org.quartz.SchedulerException;
import org.quartz.spi.InstanceIdGenerator;

public class CustomInstanceIdGenerator implements InstanceIdGenerator {

    @Override
    public String generateInstanceId() throws SchedulerException {
        return String.valueOf(System.currentTimeMillis());
    }
}
