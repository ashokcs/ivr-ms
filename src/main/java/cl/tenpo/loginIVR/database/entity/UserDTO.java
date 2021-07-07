package cl.tenpo.loginIVR.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tenpo_user")
public class UserDTO {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    private String rut;
    private String email;
    private Integer attempt;
    private Integer status;

    private LocalDateTime created;
    private LocalDateTime updated;

    public UUID getId(){
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserDTO))
            return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(this.id, userDTO.id) && Objects.equals(this.rut, userDTO.rut)
            && Objects.equals(this.email, userDTO.email) && Objects.equals(this.status, userDTO.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.rut, this.email, this.status);
    }

    @Override
    public String toString() {
        return "User{" +
            "id=" + this.id + 
            ",rut='" + this.rut + "'" +
            ",email='" + this.email + "'" +
            ",status=" + this.status +
            '}';
    }
}