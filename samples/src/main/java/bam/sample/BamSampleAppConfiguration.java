package bam.sample;

import bam.BamPlane;
import bam.opengl.OpenGlConfiguration;
import bam.sample.model.BorderWall;
import bam.sample.model.RedBall;
import lombok.extern.slf4j.Slf4j;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "bam.sample")
@Slf4j
class BamSampleAppConfiguration {
    @Bean
    SampleScene sampleScene(BamPlane bamPlane, BorderWall borderWall, RedBall redBall) {
        return new SampleScene(bamPlane, borderWall, redBall);
    }


    private class SampleScene {
        private static final float BORDER_WALL_WITH = 10f;

        @Autowired
        SampleScene(BamPlane bamPlane, BorderWall borderWall, RedBall redBall) {
            borderWall.create(BORDER_WALL_WITH);
            redBall.create();
            log.info("scene ready");
            bamPlane.start();

        }
    }

    @Bean
    BamPlane bamPlane(World world, OpenGlConfiguration openGlConfiguration) {
        return new BamPlane(world, openGlConfiguration);
    }

    @Bean
    World world() {
        final Vec2 gravity = new Vec2(0.0f, -100.0f);
        final World world = new World(gravity);
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
        world.setAllowSleep(true);
        return world;
    }

    @Bean
    OpenGlConfiguration openGlConfiguration() {
        return new OpenGlConfiguration();
    }
}
