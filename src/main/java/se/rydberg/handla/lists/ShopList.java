package se.rydberg.handla.lists;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
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

    public Set<Article> getArticles(){
        if(articles==null){
            return new HashSet<>();
        }else{
            return articles;
        }
    }
}
