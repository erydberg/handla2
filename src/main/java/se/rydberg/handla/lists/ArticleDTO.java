package se.rydberg.handla.lists;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDTO {
    private Integer id;

    @NotEmpty(message = "Vad vill du skriva upp på listan?")
    private String title;
    private boolean bought = false;
    @EqualsAndHashCode.Exclude
    private ShopList shopList;
    @EqualsAndHashCode.Exclude
    private Category category;
}
