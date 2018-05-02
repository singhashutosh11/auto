package com.thed.zephyr.cloud.rest.util;

import java.util.List;

/**
 * Created by aliakseimatsarski on 3/21/16.
 */
public class ZFJConnectResults<T> {

    public List<T> resultList;

    public int offset;

    public int totalCount;

    public int maxAllowed;

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getMaxAllowed() {
        return maxAllowed;
    }

    public void setMaxAllowed(int maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    public ZFJConnectResults(List<T> resultList, int offset, int totalCount, int maxAllowed) {

        this.resultList = resultList;
        this.offset = offset;
        this.totalCount = totalCount;
        this.maxAllowed = maxAllowed;
    }

    public ZFJConnectResults() {

    }
}
