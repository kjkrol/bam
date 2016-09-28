package bam.sample.config;

import bam.BamScene;
import bam.opengl.OpenGlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.jbox2d.dynamics.World;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BamPlaneConfig {

    @Bean
    public BamScene bamPlane(World world, OpenGlConfiguration openGlConfiguration) {
        return new BamScene(world, openGlConfiguration);
    }

    @Bean
    public OpenGlConfiguration openGlConfiguration() {
        final OpenGlConfiguration openGlConfiguration = OpenGlConfiguration.builder().build();
        log.info(openGlConfiguration.toString());
        return openGlConfiguration;
    }

}
