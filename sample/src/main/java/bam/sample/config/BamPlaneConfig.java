package bam.sample.config;

import bam.BamScene;
import bam.display.DisplayConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.jbox2d.dynamics.World;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BamPlaneConfig {

    @Bean
    public BamScene bamPlane(World world, DisplayConfiguration displayConfiguration) {
        return new BamScene(world, displayConfiguration);
    }

    @Bean
    public DisplayConfiguration openGlConfiguration() {
        final DisplayConfiguration displayConfiguration = DisplayConfiguration.builder().build();
        log.info(displayConfiguration.toString());
        return displayConfiguration;
    }

}
