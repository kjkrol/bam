package bam.sample.config;

import bam.BamPlane;
import bam.opengl.OpenGlConfiguration;
import org.jbox2d.dynamics.World;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BamPlaneConfig {

    @Bean
    public BamPlane bamPlane(World world, OpenGlConfiguration openGlConfiguration) {
        return new BamPlane(world, openGlConfiguration);
    }

    @Bean
    public OpenGlConfiguration openGlConfiguration() {
        return new OpenGlConfiguration();
    }
}
