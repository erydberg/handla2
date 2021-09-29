package se.rydberg.handla.lists;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private Integer id;
    @NotEmpty(message = "Ett namn på kategorin behövs")
    private String title;
    private int sortOrder;
    private String color;
}
