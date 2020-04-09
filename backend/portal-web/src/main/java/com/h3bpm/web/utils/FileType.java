package com.h3bpm.web.utils;

/**
 * Created by tonghao on 2020/3/1.
 */
public enum FileType {
    DIR("dir"), File("file");

    private String value;

    FileType(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return this.value;
    }
}
