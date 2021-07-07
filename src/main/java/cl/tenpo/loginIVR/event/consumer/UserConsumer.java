package cl.tenpo.loginIVR.event.consumer;

import cl.tenpo.loginIVR.event.Topics;
import cl.tenpo.loginIVR.database.repository.UserRepository;
import cl.tenpo.loginIVR.service.UserService;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class UserConsumer {

    @Autowired
    private UserService userService;

    Logger logger = LoggerFactory.getLogger(UserConsumer.class);

    @KafkaListener( topics = {Topics.USER_CREATED_TOPIC, Topics.USER_UPDATED_TOPIC, Topics.USER_ACCOUNT_CLOSED_TOPIC}
            , autoStartup = "${kafka.listener.auto.start:false}")
    public void usersEvent(@Payload String message) throws IOException, JsonMappingException {
        logger.info("UsersEvent_Msg: {}",message);
        ObjectMapper objectMapper = new ObjectMapper();
        UserEventDTO userEventDTO = objectMapper.readValue(message, UserEventDTO.class);
        userService.createUser(userEventDTO);
        logger.info("UsersEvent_User_Created_or_Updated");
    }

}