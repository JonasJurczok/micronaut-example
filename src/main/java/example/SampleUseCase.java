package example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class SampleUseCase {

    private static final Logger LOG = LoggerFactory.getLogger(SampleUseCase.class);

    public void trigger(String name) {
        LOG.info("Triggering use case for name [{}].", name);
    }
}
