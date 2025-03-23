package com.bootcamp.blog.core.models;

import java.util.List;
import java.util.Map;

public interface FooterModel {
    String getText1();

    String getText2();

    List<Map<String,String>> getNavItems();

}
