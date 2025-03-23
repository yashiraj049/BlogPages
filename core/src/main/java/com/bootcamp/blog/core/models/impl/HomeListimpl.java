package com.bootcamp.blog.core.models.impl;

import com.bootcamp.blog.core.models.HomeList;
import com.bootcamp.blog.core.models.PublishPage;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.adobe.cq.wcm.core.components.commons.editor.dialog.childreneditor.Item.LOG;

@Model(adaptables = SlingHttpServletRequest.class, adapters = HomeList.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HomeListimpl implements HomeList {

    private static final Logger LOG = LoggerFactory.getLogger(HomeList.class);
    private static final String DEFAULT_IMAGE = "/content/dam/logo1.jpg"; // Default fallback image


    @ValueMapValue
    private String publishpagepath;

    @SlingObject
    private  ResourceResolver resourceResolver;

    @SlingObject
    private Resource resource;

   @SlingObject
   private SlingHttpServletRequest request;

    private PageManager getPageManager() {
        if (resourceResolver != null) {
            return resourceResolver.adaptTo(PageManager.class);
        }
        return null;
    }

    int numberOfBlogs=0;
    @Override
    public List<Blogs> getBlogsList() {
        int TotalBlogs = 3;
        List<Blogs> blogsList = new ArrayList<>();

        if (publishpagepath== null || publishpagepath.isEmpty()) {
            LOG.warn("Parent Page Path is null or empty!");
            return null;
        }

        PageManager pageManager = getPageManager();
        if (pageManager == null) {
            LOG.error("PageManager is null! Unable to fetch pages.");
            return null;
        }

        Page currentPage = pageManager.getPage(publishpagepath);

        if (currentPage != null) {
            Iterator<Page> childPages = currentPage.listChildren();
            while (childPages.hasNext() && numberOfBlogs < TotalBlogs) {
                Page childPage = childPages.next();
                LOG.info("Processing child page: {}", childPage.getPath());

// Fetch Created Date
                Calendar created = childPage.getProperties().get("jcr:created", Calendar.class);
                if (created == null) {
                    LOG.warn("No creation date found for page: {}", childPage.getPath());
                    continue; // Skip this page if creation date is missing
                }

// Fetch Page Title and Description
                String heading = childPage.getTitle(); // jcr:title
                String subHeading = childPage.getDescription(); // jcr:description
                LOG.info("Title: {}, Description: {}", heading, subHeading);

// Format Created Date
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd");
                String formattedDate = sdf.format(created.getTime());

// Fetch Image Path
                String image = getImagePath(childPage);
                LOG.info("Final Image Path: {}", image);

// Page Link
                String link = childPage.getPath() + ".html";

// Add Blog Entry to List
                blogsList.add(new Blogs(heading, subHeading, formattedDate, image, link));
                numberOfBlogs++;
            }
        } else {
            LOG.error("Current page is null! Cannot fetch blog list.");
        }
        return blogsList;
    }

    /**
     * Fetches the image path from the child page.
     */
    private String getImagePath(Page childPage) {
        String imagePath = DEFAULT_IMAGE; // Default image
        LOG.info("Fetching image for page: {}", childPage.getPath());

// Correct path where image is stored
        String imageResourcePath = "root/responsivegrid/image/file/jcr:content";
        Resource imageResource = childPage.getContentResource(imageResourcePath);

        if (imageResource != null) {
            LOG.info("Image resource found at: {}", imageResource.getPath());
            ValueMap properties = imageResource.getValueMap();

            if (properties.containsKey("jcr:data")) { // Check if actual image binary exists
                imagePath = childPage.getPath() + "/jcr:content/root/responsivegrid/image/file";
                LOG.info("Image found, setting path: {}", imagePath);
            } else {
                LOG.warn("No 'jcr:data' found in image resource: {}", imageResource.getPath());
            }
        } else {
            LOG.warn("Image resource not found at expected path: {}/{}", childPage.getPath(), imageResourcePath);
        }

        return imagePath;
    }
}




