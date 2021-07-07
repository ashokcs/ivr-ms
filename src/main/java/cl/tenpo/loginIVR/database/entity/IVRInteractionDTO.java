package cl.tenpo.loginIVR.database.entity;

import java.util.Objects;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tenpo_ivr_interaction")
public class IVRInteractionDTO {

    @Id
    @SequenceGenerator(name="tenpo_ivr_interaction_id_sequence", sequenceName="public.tenpo_ivr_interaction_id_sequence", allocationSize=1, initialValue = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tenpo_ivr_interaction_id_sequence")
    @Column(name = "id", nullable = false)
    private Long id;
    private String call_id;
    private String rut;
    private String request;
    private String response;
    private LocalDateTime created_at;

    public IVRInteractionDTO(){}

    public IVRInteractionDTO(String call_id, String rut, String request, String response) {
        this.setCall_id(call_id);
        this.setRut(rut);
        this.setRequest(request);
        this.setResponse(response);
        this.setCreatedAt(LocalDateTime.now());
    }

    // Getter and setter
    public Long getId(){
        return id;
    }
    
    public void setId(Long id){
        this.id = id;
    }

    public String getCall_id() {
        return call_id;
    }

    public void setCall_id(String call_id) {
        this.call_id = call_id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof IVRInteractionDTO))
            return false;
        IVRInteractionDTO ivrit = (IVRInteractionDTO) o;
        return Objects.equals(this.id, ivrit.id) && Objects.equals(this.call_id, ivrit.call_id)
            && Objects.equals(this.rut, ivrit.rut) && Objects.equals(this.request, ivrit.request)
            && Objects.equals(this.response, ivrit.response) && Objects.equals(this.created_at, ivrit.created_at);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.call_id, this.rut, this.request, this.response, this.created_at);
    }

    @Override
    public String toString() {
        return "IVRInteraction{" + 
            "id=" + this.id + 
            ",call_id='" + this.call_id + "'" +
            ",rut='" + this.rut + "'" +
            ",request='" + this.request + "'" +
            ",response='" + this.response + "'" +
            ",created_at='" + this.created_at + "'" +
            '}';
    }
}