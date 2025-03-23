package com.bootcamp.blog.core.servlets;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Iterator;

@Component(service = Servlet.class, name = "Bootcampblog" , property = {
        "sling.servlet.resourceTypes="+ "bootcampblog/components/page",
        "sling.servlet.extensions=" + "html",
        "sling.servlet.selectors="+"abc"
})
//path :http://localhost:4502/content/bootcampblog/newnode.abc.html?path=/content/bootcampblog/us/en/blogs-list
public class BootcampblogServlet extends SlingSafeMethodsServlet {
    @Override
    protected void doGet( SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String requestParam = request.getParameter("path");
        ResourceResolver resolver = request.getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);

        Page parentPage = pageManager.getPage(requestParam);
        JsonArray childPagesJson = new JsonArray();
        Iterator<Page> childPages = parentPage.listChildren();
        while(childPages.hasNext()){
            Page childPage = childPages.next();
            JsonObject object = new JsonObject();
            String title = childPage.getTitle();
            String path = childPage.getPath();
            String description = childPage.getDescription();

            object.addProperty("title" , title);
            object.addProperty("path" , path);
            object.addProperty("description" , description);

            childPagesJson.add(object);
        }
        response.getWriter().write(childPagesJson.toString());
    }
}
