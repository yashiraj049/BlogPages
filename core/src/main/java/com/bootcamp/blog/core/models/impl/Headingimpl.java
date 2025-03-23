package com.bootcamp.blog.core.models.impl;

import com.day.cq.wcm.api.Page;
import com.bootcamp.blog.core.models.Heading;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;


@Model(adaptables = SlingHttpServletRequest.class, adapters = Heading.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Headingimpl implements Heading {

    @Via("resource")
    @Inject
    private String title;


    @ScriptVariable
    private Page currentPage;

    @Override
    public String getPageTitle() {
        if(currentPage==null)
            return "page not found";
        return (title == null) ? currentPage.getProperties().get("jcr:title", String.class):title;
    }
    @Override
    public String getDescription(){
        if(currentPage==null)
            return "page not found";
        return  currentPage.getProperties().get("jcr:description",String.class);
    }

    @Override
    public String getFormattedDate() {

        return getPageCreationDate();
    }

    @Override
    public String getAuthorName() {

        return currentPage.getProperties().get("jcr:createdBy", String.class);
    }

    private String getPageCreationDate() {
        if(currentPage==null)
            return "page not found";
        Calendar calendar = currentPage.getProperties().get("jcr:created", Calendar.class);
        if (calendar!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            return sdf.format(calendar.getTime());
        }
        return"No Date found";

    }
}
