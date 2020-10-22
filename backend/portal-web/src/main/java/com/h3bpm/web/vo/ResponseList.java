package com.h3bpm.web.vo;

import java.util.List;
import java.util.Map;

/**
 * This class represents a HTTP responseList.
 *
 * @author lzf
 */
public class ResponseList {
    private List<Map<String, Object>> result = null;

    public ResponseList(List<Map<String, Object>> result) {
        this.result = result;
    }

    /**
     * Returns the result of a success request.
     *
     * @return A JSON data (in a map format), never be null.
     */
    public List<Map<String, Object>> getResult() {
        return result;
    }
}
