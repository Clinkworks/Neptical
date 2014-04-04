package com.clinkworks.neptical.util;

import static com.clinkworks.neptical.util.PathUtil.firstSegment;
import static com.clinkworks.neptical.util.PathUtil.lastSegment;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PathUtilUnitTest {
    @Test
    public void allPathMethodsCanHandleAnEmptyAndNullString() {
        String path = "";

        assertEquals("", firstSegment(path));
        assertEquals("", firstSegment(null));
        assertEquals("", lastSegment(path));
        assertEquals("", lastSegment(null));
    }
}
