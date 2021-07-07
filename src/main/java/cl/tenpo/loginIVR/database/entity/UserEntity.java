package cl.tenpo.loginIVR.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;
    @Column(name = "tributary_identifier")
    private String rut;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTributaryIdentifier() {
        return rut;
    }

    public void setTributaryIdentifier(String rut) {
        this.rut = rut;
    }
}
