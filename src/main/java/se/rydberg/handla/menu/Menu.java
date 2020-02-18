package se.rydberg.handla.menu;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class Menu {

    @Id
    private int id;
    private String title;
    private String description;
    private Date dayToEat;
    private String descriptionUrl;

}
