package se.rydberg.handla.lists;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "shoplist")
public class ShopList {

    @Id
    @GeneratedValue
    private Integer id;
    @NotEmpty(message = "Listan behöver ha ett namn")
    private String title;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    @OrderBy("bought,title asc")
    @JoinColumn(name = "fk_shoplist")
    @EqualsAndHashCode.Exclude
    private Set<Article> articles = new LinkedHashSet<>();
    private boolean useCategory;

    public Set<Article> getArticles(){
        if(articles==null){
            return new LinkedHashSet<>();
        }else{
            return articles;
        }
    }
}
