package cl.tenpo.loginIVR.service;

import cl.tenpo.loginIVR.database.entity.UserDTO;
import cl.tenpo.loginIVR.event.consumer.UserEventDTO;

public interface UserService {
    UserDTO createUser(UserEventDTO userEventDto);
}