package se.rydberg.handla.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDTO {
    private Integer id;
    @NotEmpty(message = "Fyll i maträttens titel")
    private String title;
    private String description;
    private LocalDate dayToEat;
    @URL(message = "Länken behöver vara korrekt")
    private String descriptionUrl;
    private Grade grade = Grade.UN_GRADED;
    private boolean eaten;
}
