package se.rydberg.handla.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
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
    @Size(max = 4000, message = "Inte mer än 4000 tecken för beskrivningen.")
    private String description;
    private LocalDate dayToEat;
    @URL(message = "Länken behöver vara korrekt")
    private String descriptionUrl;
    private Grade grade = Grade.UN_GRADED;
    private boolean eaten;
    private String imageId;
}
