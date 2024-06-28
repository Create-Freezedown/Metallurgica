package com.freezedown.metallurgica.content.fluids.types.uf_backport.behaviours;

import java.util.List;

public interface IBehaviorable {
    List<IFluidBehavior> getBehaviors();
    
    @SuppressWarnings("unchecked")
    default <T extends IFluidBehavior> List<T> getBehavior(Class<T> behavior) {
        return (List<T>) getBehaviors().stream().filter(behavior::isInstance).toList();
    }
}
