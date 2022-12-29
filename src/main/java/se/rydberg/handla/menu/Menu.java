package se.rydberg.handla.menu;

import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    @Column(length = 4000)
    private String description;
    private LocalDate dayToEat;
    private String descriptionUrl;
    @Enumerated(EnumType.STRING)
    private Grade grade = Grade.UN_GRADED;
    private String imageId;
    private FoodTime foodTime;
}
