package bam.sample.config;

import bam.BamScene;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "bam.sample")
@Slf4j
public class BamSampleAppConfig {

    @Autowired
    public BamSampleAppConfig(BamScene bamScene, SampleScene sampleScene) {
        sampleScene.init(bamScene);
        bamScene.start();
    }

}
