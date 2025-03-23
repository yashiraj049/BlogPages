package com.bootcamp.blog.core.models;

import java.util.Map;
import java.util.Set;

public interface Archive {
    String getParentPagePath();
    Set<Map<String, String>> getFormattedDates();
}
