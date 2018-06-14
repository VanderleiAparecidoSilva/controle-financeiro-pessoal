package com.vanderlei.cfp.job.impl;

import com.vanderlei.cfp.job.CFPJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"live", "dev", "integration"})
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class VencimentoJob extends CFPJob {
}
