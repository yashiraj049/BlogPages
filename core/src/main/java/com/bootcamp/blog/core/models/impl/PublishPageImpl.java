package com.bootcamp.blog.core.models.impl;

import com.bootcamp.blog.core.models.PublishPage;
import com.bootcamp.blog.core.service.BlogService;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

@Model(adaptables = SlingHttpServletRequest.class, adapters = PublishPage.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PublishPageImpl implements PublishPage {

    private static final Logger LOG = LoggerFactory.getLogger(PublishPageImpl.class);
    private static final String DEFAULT_IMAGE = "/content/dam/logo1.jpg"; // Default fallback image


    @OSGiService
    private  BlogService blogService;

    @ScriptVariable
    private Page currentPage; // Injects the current page

    @SlingObject
    private ResourceResolver resourceResolver; // Allows access to JCR nodes

    @SlingObject
    private SlingHttpServletRequest request; // Inject the request to get query parameters

    private List<Blog> blogList = new ArrayList<>();

    @PostConstruct
    protected void init() {
        fetchBlogPages();
    }

    private void fetchBlogPages() {
        if (currentPage == null) {
            LOG.error("Current page is null! Cannot fetch blog list.");
            return;
        }

        // Retrieve request parameters
        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");

        LOG.info("Received request parameters -> Year: {}, Month: {}", yearParam, monthParam);

        Iterator<Page> childPages = currentPage.listChildren();
        int counter=0;
        while (childPages.hasNext() && counter < blogService.getDefaultBlogCount()) {
            Page childPage = childPages.next();
            LOG.info("Processing child page: {}", childPage.getPath());

            // Fetch Created Date
            Calendar created = childPage.getProperties().get("jcr:created", Calendar.class);
            if (created == null) {
                LOG.warn("No creation date found for page: {}", childPage.getPath());
                continue; // Skip this page if creation date is missing
            }

            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);

            String createdYear = yearFormat.format(created.getTime());
            String createdMonth = monthFormat.format(created.getTime());

            LOG.info("Page Created: Year = {}, Month = {}", createdYear, createdMonth);

            // Filter based on request parameters (if provided)
            if (yearParam != null && monthParam != null) {
                if (!createdYear.equals(yearParam) || !createdMonth.equals(monthParam)) {
                    LOG.info("Skipping page {} as it does not match the filter", childPage.getPath());
                    continue; // Skip pages that do not match the filter
                }
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
            blogList.add(new Blog(heading, subHeading, formattedDate, image, link));
            counter++;
        }
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

    @Override
    public List<Blog> getBlogList() {
        return blogList;
    }
}
