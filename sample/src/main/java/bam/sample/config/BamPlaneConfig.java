package bam.sample.config;

import bam.BamScene;
import bam.display.DisplayParams;
import lombok.extern.slf4j.Slf4j;
import org.jbox2d.dynamics.World;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BamPlaneConfig {

    @Bean
    public BamScene bamPlane(World world, DisplayParams displayParams) {
        return new BamScene(world, displayParams);
    }

    @Bean
    public DisplayParams openGlConfiguration() {
        final DisplayParams displayParams = DisplayParams.builder().build();
        log.info(displayParams.toString());
        return displayParams;
    }

}
