package com.bootcamp.blog.core.service.impl;

import com.bootcamp.blog.core.config.BlogConfig;
import com.bootcamp.blog.core.service.BlogService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = BlogService.class, immediate = true)
@Designate(ocd = BlogConfig.class)
public class BlogServiceImpl implements BlogService {

    private int defaultBlogCount;

    @Activate
    @Modified
    protected void activate(BlogConfig config) {
        this.defaultBlogCount = config.defaultBlogCount();
    }

    @Override
    public int getDefaultBlogCount() {
        return defaultBlogCount;
    }
}


