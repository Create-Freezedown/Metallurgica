package dev.metallurgists.metallurgica.foundation.util;

import javax.annotation.Nullable;

public interface CommentsTracker {
    void addComment(String key, String comment);
    
    @Nullable
    String getComment(String key);
}
