package com.bootcamp.blog.core.models.impl;

import com.bootcamp.blog.core.models.Archive;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

@Model(adaptables = {SlingHttpServletRequest.class}, adapters = Archive.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Archiveimpl implements Archive {

    @Self
    private SlingHttpServletRequest request;

    @ValueMapValue
    private String parentPagePath;//

    private Set<Map<String, String>> formattedDates = new LinkedHashSet<>();

    @PostConstruct
    protected void init() {
        if (parentPagePath != null && !parentPagePath.isEmpty()) {
            ResourceResolver resourceResolver = request.getResourceResolver();
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            if (pageManager != null)
            {
                Page parentPage = pageManager.getPage(parentPagePath);
                if (parentPage != null) {
                    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM",  Locale.ENGLISH);

                    for (Iterator<Page> it = parentPage.listChildren(); it.hasNext(); ) {
                        Page childPage = it.next();
                        Calendar createdDate = childPage.getProperties().get("jcr:created", Calendar.class);
                        if (createdDate != null) {
                            Map<String, String> dateMap = new HashMap<>();
                            dateMap.put("year", yearFormat.format(createdDate.getTime()));
                            dateMap.put("month", monthFormat.format(createdDate.getTime()));
                            formattedDates.add(dateMap);
                        }
                    }
                }
            }
        }
    }

    @Override
    public Set<Map<String, String>> getFormattedDates() {
        return formattedDates;
    }

    @Override
    public String getParentPagePath() {
        return parentPagePath;
    }
}