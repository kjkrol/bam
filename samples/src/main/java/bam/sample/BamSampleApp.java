package bam.sample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@Slf4j
public class BamSampleApp {

    public static void main(String[] args) {
        new AnnotationConfigApplicationContext(BamSampleAppConfiguration.class);
    }

}
