package com.freezedown.metallurgica.experimental.exposure_effects;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BlurryAt {
    int value();
}
