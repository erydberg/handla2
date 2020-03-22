package se.rydberg.handla.menu;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue
    private Integer id;
    private String title;
    private String description;
    private Date dayToEat;
    private String descriptionUrl;

}
