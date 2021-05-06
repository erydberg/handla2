package se.rydberg.handla.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageFileMetaData {
    private String name;
    private String url;
    private String type;
    private long size;
}
