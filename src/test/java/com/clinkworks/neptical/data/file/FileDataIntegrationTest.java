package com.clinkworks.neptical.data.file;

import static org.junit.Assert.assertNotNull;

import java.io.File;

import org.junit.Test;

import com.clinkworks.neptical.data.Data;

public class FileDataIntegrationTest {

    @Test
    public void ensureFileDataCanLoadResourcesDirectory() {
        File file = new File(Thread.currentThread().getContextClassLoader().getResource("data/").getFile());

        FileData fileData = new FileData("", "", null, null, file);

        Data data = fileData.find("users");

        assertNotNull(data);
    }
}
