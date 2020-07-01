package se.rydberg.handla.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue
    private Integer id;
    @NotEmpty(message ="Fyll i maträttens titel")
    private String title;
    private String description;
    private LocalDate dayToEat;
    @URL(message = "Länken behöver vara korrekt")
    private String descriptionUrl;
    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.UN_GRADED;
    private boolean eaten;
}
