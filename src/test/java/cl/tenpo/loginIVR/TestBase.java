package cl.tenpo.loginIVR;

import cl.tenpo.loginIVR.externalservice.user.dto.UserResponse;
import cl.tenpo.loginIVR.model.UserStatusEnum;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;


@TestPropertySource(properties = {
        "spring.datasource.driver-class-name = org.h2.Driver",
        "spring.datasource.url = jdbc:h2:mem:test;INIT=CREATE SCHEMA IF NOT EXISTS tenpo",
        "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
        "spring.datasource.username = sa",
        "spring.datasource.password = sa",
        "spring.jpa.hibernate.ddl-auto = create-drop",
        "kafka.listen.auto.start=false"
})
public abstract class TestBase {

    public UserResponse getRandomUserResponseDTO(String email) {
        return UserResponse.builder()
                .id(UUID.randomUUID())
                .state(UserStatusEnum.ACTIVE)
                .email(email)
                .build();
    }
}