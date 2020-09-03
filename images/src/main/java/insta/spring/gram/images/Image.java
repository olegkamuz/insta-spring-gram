package insta.spring.gram.images;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Image {

    @Id
    final private String id;
    final private String name;

}
