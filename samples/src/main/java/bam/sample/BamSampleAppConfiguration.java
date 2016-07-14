package bam.sample;

import bam.OpenGlConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BamSampleAppConfiguration {

//    @Qualifier("bamPlane")
//    private final BamPlane bamPlane;

//    @Autowired
//    public BamSampleAppConfiguration() {
        //BamPlane bamPlane) {
//        this.bamPlane = bamPlane;
//        bamPlane.start();
//    }

//    @Bean
//    BamPlane bamPlane(@Qualifier("world")World world, OpenGlConfiguration openGlConfiguration) {
//        return new BamPlane(world, openGlConfiguration);
//    }
//
//    @Bean
//    World world() {
//        final Vec2 gravity = new Vec2(0.0f, -100.0f);
//        final World world = new World(gravity);
//        world.setContactListener(new ContactListener() {
//            @Override
//            public void beginContact(Contact contact) {
//            }
//
//            @Override
//            public void endContact(Contact contact) {
//            }
//
//            @Override
//            public void preSolve(Contact contact, Manifold oldManifold) {
//            }
//
//            @Override
//            public void postSolve(Contact contact, ContactImpulse impulse) {
//            }
//        });
//        world.setAllowSleep(true);
//        return world;
//    }

    @Bean
    OpenGlConfiguration openGlConfiguration() {
        return new OpenGlConfiguration(new JarNativeLibsScanner());
    }

}
