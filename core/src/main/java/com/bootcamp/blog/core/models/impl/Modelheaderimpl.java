package com.bootcamp.blog.core.models.impl;

import com.adobe.cq.social.site.api.ExtendedNav;
import com.bootcamp.blog.core.models.Modelheader;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Model(adaptables = {Resource.class}, adapters =Modelheader .class,
defaultInjectionStrategy=DefaultInjectionStrategy.OPTIONAL)
public class Modelheaderimpl implements Modelheader {


    @ValueMapValue
    private String logoText;

    @ValueMapValue
    private String logoImage;

//    @ChildResource(name = "navItems")
//    private List<Resource> navItems; // Fetch child resources under 'navItems'

  @SlingObject
  private Resource componentResource;


    @Override
    public String getLogoImage() {
        return logoImage;
    }

    @Override
    public String getLogoText() {
        return logoText;
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
