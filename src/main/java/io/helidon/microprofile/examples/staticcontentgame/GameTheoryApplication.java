
package io.helidon.microprofile.examples.staticcontentgame;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationScoped
@ApplicationPath("/")
public class GameTheoryApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Set.of(GameTheoryResource.class);
    }
}
