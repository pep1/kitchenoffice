package com.pep1.kitchenoffice.data;

import com.pep1.kitchenoffice.server.storage.Storable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
@Getter
@Setter
@NoArgsConstructor
public class Image extends AbstractPersistable implements Storable {

    private static final String STORAGE_TYPE = "image";

    private String fileName;

    private String mimeType;

    private long size;

    private int width;

    private int height;

    public String getStorageType() {
        return STORAGE_TYPE;
    }

}
