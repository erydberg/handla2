package se.rydberg.handla.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.URL;
import se.rydberg.handla.image.ImageFileMetaData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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
