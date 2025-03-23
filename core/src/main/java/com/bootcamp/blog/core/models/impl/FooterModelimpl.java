package com.bootcamp.blog.core.models.impl;

import com.bootcamp.blog.core.models.FooterModel;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Model(adaptables = Resource.class , adapters = FooterModel.class , defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FooterModelimpl implements FooterModel {

    @ValueMapValue
    private String text1;

    @ValueMapValue
    private String text2;

    @Self
    private Resource componentResource;


    @Override
    public String getText1() {
        return text1;
    }

    @Override
    public String getText2() {
        return text2;
    }

    @Override
    public List<Map<String, String>> getNavItems() {
        List<Map<String, String>> navItemsList = new ArrayList<>();
        Resource navItems=componentResource.getChild("actions");
        if (navItems != null) {
            for (Resource navItem : navItems.getChildren()) {
                Map<String, String> linkMap = new HashMap<>();
                linkMap.put("Title", navItem.getValueMap().get("navTitle", String.class));
                linkMap.put("Link", navItem.getValueMap().get("navLink", String.class));
                navItemsList.add(linkMap);
            }
        }
        return navItemsList;
    }
}
