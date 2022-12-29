package se.rydberg.handla.lists;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;

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
