package com.bootcamp.blog.core.models;

import java.util.List;

public interface HomeList {
    public List<Blogs> getBlogsList();

    /**
     * Inner class representing a blog entry.
     */
    class Blogs{
        private final String heading;
        private final String subHeading;
        private final String date;
        private final String image;
        private final String link;

        public Blogs(String heading, String subHeading, String date, String image, String link) {
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
