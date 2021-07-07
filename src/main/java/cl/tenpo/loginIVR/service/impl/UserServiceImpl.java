package cl.tenpo.loginIVR.service.impl;

import cl.tenpo.loginIVR.database.entity.UserDTO;
import cl.tenpo.loginIVR.database.repository.UserRepository;
import cl.tenpo.loginIVR.event.consumer.UserEventDTO;
import cl.tenpo.loginIVR.service.UserService;
import cl.tenpo.loginIVR.util.ObjectMapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private static Map<String, UserDTO> usersRutWhitelist = new ConcurrentHashMap<>();
    private static Map<UUID, UserDTO> usersIdWhitelist = new ConcurrentHashMap<>();

    @Override
    public UserDTO createUser(UserEventDTO userEventDto) {
        log.info("UserServiceImpl_createUser: "+userEventDto.toString());
        return ObjectMapperUtils.map(userRepository.save(ObjectMapperUtils.map(userEventDto, UserDTO.class)), UserDTO.class);
    }
}
