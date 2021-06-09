package se.rydberg.handla.lists;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Data
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
