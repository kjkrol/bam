package bam.sample.config;

import bam.BamPlane;
import bam.opengl.OpenGlConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.jbox2d.dynamics.World;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BamPlaneConfig {

    @Bean
    public BamPlane bamPlane(World world, OpenGlConfiguration openGlConfiguration) {
        return new BamPlane(world, openGlConfiguration);
    }

    @Bean
    public OpenGlConfiguration openGlConfiguration() {
        final OpenGlConfiguration openGlConfiguration = OpenGlConfiguration.builder().build();
        log.info(openGlConfiguration.toString());
        return openGlConfiguration;
    }

}
