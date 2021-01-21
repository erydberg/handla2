package se.rydberg.handla.lists;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopListDTO {

    private Integer id;
    @NotEmpty(message = "Listan behöver ha ett namn")
    private String title;
    @EqualsAndHashCode.Exclude
    private Set<Article> articles = new LinkedHashSet<>();

    public Set<Article> getArticles() {
        if (articles == null) {
            return new HashSet<>();
        } else {
            return articles;
        }
    }
}
