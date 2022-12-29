package se.rydberg.handla.lists;

import lombok.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue
    private Integer id;

    @NotEmpty(message = "Vad vill du skriva upp på listan?")
    private String title;

    private boolean bought = false;

    @ManyToOne
    @JoinColumn(name = "fk_shoplist")
    @EqualsAndHashCode.Exclude
    private ShopList shopList;

    @ManyToOne
    @JoinColumn(name = "fk_category")
    @EqualsAndHashCode.Exclude
    private Category category;
}
