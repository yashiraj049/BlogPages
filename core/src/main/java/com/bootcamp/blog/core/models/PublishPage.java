package com.bootcamp.blog.core.models;

import java.util.List;
import java.util.Map;

public interface PublishPage {
    List<Blog> getBlogList();

    /**
     * Inner class representing a blog entry.
     */
    class Blog {
        private final String heading;
        private final String subHeading;
        private final String date;
        private final String image;
        private final String link;

        public Blog(String heading, String subHeading, String date, String image, String link) {
            this.heading = heading;
            this.subHeading = subHeading;
            this.date = date;
            this.image = image;
            this.link = link;
        }

        public String getHeading() {
            return heading;
        }

        public String getSubHeading() {
            return subHeading;
        }

        public String getDate() {
            return date;
        }

        public String getImage() {
            return image;
        }

        public String getLink() {
            return link;
        }
    }
}


