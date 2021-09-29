package se.rydberg.handla.lists;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue
    private Integer id;

    @NotEmpty(message = "Ett namn på kategorin behövs")
    private String title;

    private int sortOrder;

    private String color;

    public String getCss() {
        return "border-left:5px solid " + (color == null ? "grey" : color);
    }
}
