package bam.sample.config;

import bam.BamPlane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "bam.sample")
@Slf4j
public class BamSampleAppConfig {

    @Autowired
    public BamSampleAppConfig(BamPlane bamPlane, SampleScene sampleScene) {
        sampleScene.init(bamPlane);
        bamPlane.start();
    }

}
