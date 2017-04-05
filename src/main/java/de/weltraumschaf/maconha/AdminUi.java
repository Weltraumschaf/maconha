package de.weltraumschaf.maconha;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.vaadin.spring.events.EventBus;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.components.DisclosurePanel;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.grid.MGrid;
import org.vaadin.viritin.label.RichText;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * https://github.com/mstahv/spring-data-vaadin-crud
 */
@Theme("valo")
@SpringUI(path = "/admin")
@Title("Maconha - Admin")
public final class AdminUi extends UI {
    //        @Autowired
    //        MyService myService;

    EventBus.UIEventBus eventBus;

    private MGrid<Person> list = new MGrid<>(Person.class)
        .withProperties("id", "name", "email")
        .withColumnHeaders("id", "Name", "Email")
        // not yet supported by V8
        //.setSortableProperties("name", "email")
        .withFullWidth();
    private MTextField filterByName = new MTextField()
        .withPlaceholder("Filter by name");
    private Button addNew = new MButton(VaadinIcons.PLUS, this::add);
    private Button edit = new MButton(VaadinIcons.PENCIL, this::edit);
    private Button delete = new ConfirmButton(VaadinIcons.TRASH,
        "Are you sure you want to delete the entry?", this::remove);

    @Override
    protected void init(final VaadinRequest request) {
        DisclosurePanel aboutBox = new DisclosurePanel("Spring Boot JPA CRUD example with Vaadin UI", new RichText().withMarkDownResource("/welcome.md"));
        setContent(
            new MVerticalLayout(
                aboutBox,
                new MHorizontalLayout(filterByName, addNew, edit, delete),
                list
            ).expand(list)
        );

        // Listen to change events emitted by PersonForm see onEvent method
        eventBus.subscribe(this);
    }

    public void add(final Button.ClickEvent clickEvent) {

    }

    public void edit(final Button.ClickEvent clickEvent) {

    }

    public void remove() {

    }

    @Entity
    public static class Person implements Serializable {

        private static final long serialVersionUID = 1L;

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long id;

        @Temporal(TemporalType.TIMESTAMP)
        private Date birthDay;

        @NotNull(message = "Name is required")
        @Size(min = 3, max = 50, message = "name must be longer than 3 and less than 40 characters")
        private String name;

        private Boolean colleague;

        private String phoneNumber;

        @NotNull(message = "Email is required")
        @Pattern(regexp = ".+@.+\\.[a-z]+", message = "Must be valid email")
        private String email;

        public Person() {
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Date getBirthDay() {
            return birthDay;
        }

        public void setBirthDay(Date birthDay) {
            this.birthDay = birthDay;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean getColleague() {
            return colleague;
        }

        public void setColleague(Boolean colleague) {
            this.colleague = colleague;
        }
    }
}
