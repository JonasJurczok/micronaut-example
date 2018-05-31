package example;

import io.micronaut.scheduling.annotation.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
public class HelloWorldJob {
    private static final Logger LOG = LoggerFactory.getLogger(HelloWorldJob.class);

    private final SampleUseCase useCase;

    public HelloWorldJob(SampleUseCase useCase) {
        this.useCase = useCase;
    }

    @Scheduled(fixedDelay = "10s")
    public void every10() {
        useCase.trigger(new SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(new Date()));
    }
}
