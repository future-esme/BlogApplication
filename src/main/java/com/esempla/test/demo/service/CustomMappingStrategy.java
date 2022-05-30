package com.esempla.test.demo.service;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import org.springframework.stereotype.Service;

@Service
class CustomMappingStrategy<T> extends ColumnPositionMappingStrategy<T> {
    private static final String[] HEADER = new String[]{"comment_id", "content", "create_time", "user_id", "post_id"};

}
