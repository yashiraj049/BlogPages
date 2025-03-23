package com.bootcamp.blog.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Publish Page Sling Model Configuration",
        description = "Configuration for the Publish Page Sling Model"
)
public @interface BlogConfig{

    @AttributeDefinition(
            name = "Default Blog Count",
            description = "Number of default blogs to display if no child pages exist."
    )
    int defaultBlogCount() default 5;

}
