package cl.tenpo.loginIVR.database.entity;

import java.util.UUID;
import java.util.Objects;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenpo_ivr")
public class IVRDTO {

    @Id
    private UUID user_id;
    private UUID token ;
    private String rut;
    private LocalDateTime created_at;
    private LocalDateTime expired_at;

    public IVRDTO(){}

    public IVRDTO(UUID user_id, String rut) {
        this.user_id = user_id;
        this.rut = rut;
        this.token = UUID.randomUUID();
        this.created_at = LocalDateTime.now();
        this.expired_at = LocalDateTime.now().plusMinutes(30);
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(final UUID token) {
        this.token = token;
    }

    public UUID getUserID() {
        return user_id;
    }

    public void setUserID(final UUID user_id) {
        this.user_id = user_id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(final String rut) {
        this.rut = rut;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(final LocalDateTime createdAt) {
        this.created_at = createdAt;
    }

    public LocalDateTime getExpiredAt() {
        return expired_at;
    }

    public void setExpiredAt(final LocalDateTime expired_at) {
        this.expired_at = expired_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof IVRDTO))
            return false;
        IVRDTO IVRDTO = (IVRDTO) o;
        return Objects.equals(this.token, IVRDTO.token)
                && Objects.equals(this.user_id, IVRDTO.user_id) && Objects.equals(this.rut, IVRDTO.rut)
                && Objects.equals(this.created_at, IVRDTO.created_at) && Objects.equals(this.expired_at, IVRDTO.expired_at) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.token, this.user_id, this.rut, this.created_at, this.expired_at);
    }

    @Override
    public String toString() {
        return "IVR{" +
                "token='" + this.token + "'" +
                ",user_id=" + this.user_id +
                ",rut='" + this.rut + "'" +
                ",created_at='" + this.created_at + "'" +
                ",expirated_at='" + this.expired_at + "'" +
                '}';
    }
}