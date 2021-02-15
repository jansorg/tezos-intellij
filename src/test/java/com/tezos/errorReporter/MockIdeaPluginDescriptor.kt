package com.tezos.errorReporter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * Has to be Java because we're supporting older builds. Later builds added new methods in the interface. This isn't possible to implement
 * in Kotlin when we're also compiling with an interface which doesn't define this yet (override keyword vs Java's annotation).
 * @author jansorg
 */
public class MockIdeaPluginDescriptor extends com.tezos.errorReporter.AbstractMockIdeaPluginDescriptor {
    public MockIdeaPluginDescriptor(@NotNull String id, @NotNull String name, @NotNull String version) {
        super(id, name, version);
    }

    @Nullable
//    @Override
    public Date getReleaseDate(){
        return null;
    }

    @Nullable
//    @Override
    public String getProductCode() {
        return null;
    }

//    @Override
    public int getReleaseVersion() {
        return 0;
    }
}
