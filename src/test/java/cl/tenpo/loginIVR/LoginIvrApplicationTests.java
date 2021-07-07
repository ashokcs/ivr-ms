package cl.tenpo.loginIVR;

import cl.tenpo.loginIVR.api.controller.LoginIVRController;
import cl.tenpo.loginIVR.externalservice.user.UserRestClientImpl;
import cl.tenpo.loginIVR.model.ErrorConstants;
import cl.tenpo.loginIVR.model.ErrorConstantsEnum;
import cl.tenpo.loginIVR.api.dto.RequestDTO;
import cl.tenpo.loginIVR.api.dto.ResponseDTO;
import cl.tenpo.loginIVR.properties.UsersProps;
import cl.tenpo.loginIVR.service.PasswordValidationService;
import cl.tenpo.loginIVR.service.RUTValidationService;
import cl.tenpo.loginIVR.service.impl.RUTValidationServiceImpl;
import cl.tenpo.loginIVR.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

import cl.tenpo.loginIVR.component.LoginMSClient;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;

import java.net.UnknownHostException;

@SpringBootTest
@RunWith(SpringRunner.class)
class LoginIvrApplicationTests extends TestBase {

	static Logger logger = LoggerFactory.getLogger(LoginIvrApplicationTests.class);

	@MockBean
	private LoginMSClient loginMSClient;

	@Autowired
	private PasswordValidationService passwordValidationService;

	@Autowired
	private LoginIVRController controller;

	@MockBean
	private UserRestClientImpl userRestClient;

	@Autowired
	private RUTValidationServiceImpl rutValidationService;

	@Autowired
	private UsersProps usersProps;

	@Test
	public void contexLoads() throws Exception {
		assertNotNull(controller);
	}

	@Test
	void paramValidation()
	{
		RequestDTO requestDTO = new RequestDTO("23423494-3", "1234567891234567891234", "example@email.com", "1234");
		ResponseDTO responseDTO = new ResponseDTO();
		boolean isValid = ValidationUtil.validate(requestDTO, responseDTO,false);
		logger.info("isvalid "+isValid);
		logger.info("response "+ responseDTO);
		assertTrue(isValid);
	}

	@Test
	void controllerValidationWithRutAPI()
	{
		RequestDTO requestDTO = new RequestDTO("23423494-3", "1234567891234567891234", "example@email.com", "1234");
		controller.validateRut(requestDTO);
	}

	@Test
	void paramValidationWithPassword()
	{
		RequestDTO requestDTO = new RequestDTO("23423494-3", "1234567891234567891234", "example@email.com", "1234");
		ResponseDTO responseDTO = new ResponseDTO();
		boolean isValid = ValidationUtil.validate(requestDTO, responseDTO,true);
		logger.info("isvalid "+isValid);
		logger.info("response "+ responseDTO);
		assertTrue(isValid);
	}

	@Test
	void paramValidationEmptyRUT()
	{
		RequestDTO requestDTO = new RequestDTO("", "1234567891234567891234", "example@email.com", "1234");
		ResponseDTO responseDTO = new ResponseDTO();
		boolean isValid = ValidationUtil.validate(requestDTO, responseDTO,false);
		logger.info("isvalid "+ isValid);
		logger.info("response "+ responseDTO);
		assertFalse(isValid);
		assertEquals(ErrorConstants.EMPTY_RUT, responseDTO.getCode());
		assertEquals(ErrorConstantsEnum.BAD_REQUEST_RUT.toString(), responseDTO.getStatus());
	}

	@Test
	void paramValidationInvalidRUT()
	{
		RequestDTO requestDTO = new RequestDTO("32344-3", "1234567891234567891234", "example@email.com", "1234");
		ResponseDTO responseDTO = new ResponseDTO();
		boolean isValid = ValidationUtil.validate(requestDTO, responseDTO,false);
		logger.info("isvalid "+isValid);
		logger.info("response "+ responseDTO);
		assertFalse(isValid);
		assertEquals(ErrorConstants.INVALID_RUT_FORMAT, responseDTO.getCode());
		assertEquals(ErrorConstantsEnum.NOT_ACCEPTABLE_RUT_FORMAT.toString(), responseDTO.getStatus());
	}

	@Test
	void paramValidationRUTWithk()
	{
		RequestDTO requestDTO = new RequestDTO("323443-k", "1234567891234567891234", "example@email.com", "1234");
		ResponseDTO responseDTO = new ResponseDTO();
		boolean isValid = ValidationUtil.validate(requestDTO, responseDTO,false);
		logger.info("isvalid "+isValid);
		logger.info("response "+ responseDTO);
		assertTrue(isValid);
	}

	@Test
	void paramValidationRUTWithDots()
	{
		RequestDTO requestDTO = new RequestDTO("32.34.433-k", "1234567891234567891234", "example@email.com", "1234");
		ResponseDTO responseDTO = new ResponseDTO();
		boolean isValid = ValidationUtil.validate(requestDTO, responseDTO,false);
		logger.info("isvalid "+isValid);
		logger.info("response "+ responseDTO);
		assertTrue(isValid);
	}

	@Test
	void paramValidationRUTWithK()
	{
		RequestDTO requestDTO = new RequestDTO("312344-K", "1234567891234567891234", "example@email.com", "1234");
		ResponseDTO responseDTO = new ResponseDTO();
		boolean isValid = ValidationUtil.validate(requestDTO, responseDTO,false);
		logger.info("isvalid "+isValid);
		logger.info("response "+ responseDTO);
		assertTrue(isValid);
	}

	@Test
	void paramValidationRUTWithEightDigits()
	{
		RequestDTO requestDTO = new RequestDTO("31233344-K", "1234567891234567891234", "example@email.com", "1234");
		ResponseDTO responseDTO = new ResponseDTO();
		boolean isValid = ValidationUtil.validate(requestDTO, responseDTO,false);
		logger.info("isvalid "+isValid);
		logger.info("response "+ responseDTO);
		assertTrue(isValid);
	}

	@Test
	void paramValidationEmptyCallID()
	{
		RequestDTO requestDTO = new RequestDTO("3233494-3", "", "example@email.com","1234");
		ResponseDTO responseDTO = new ResponseDTO();
		boolean isValid = ValidationUtil.validate(requestDTO, responseDTO,false);
		logger.info("isvalid "+isValid);
		logger.info("response "+ responseDTO);
		assertFalse(isValid);
		assertEquals(ErrorConstants.EMPTY_CALL_ID, responseDTO.getCode());
		assertEquals(ErrorConstantsEnum.BAD_REQUEST_CALL_ID.toString(), responseDTO.getStatus());
	}

	@Test
	void paramValidationEmptyPassword()
	{
		RequestDTO requestDTO = new RequestDTO("23423494-3", "1234567891234567891234", "example@email.com", "");
		ResponseDTO responseDTO = new ResponseDTO();
		boolean isValid = ValidationUtil.validate(requestDTO, responseDTO,true);
		logger.info("isvalid "+isValid);
		logger.info("response "+ responseDTO);
		assertFalse(isValid);
		assertEquals(ErrorConstants.EMPTY_PASSWORD, responseDTO.getCode());
		assertEquals(ErrorConstantsEnum.BAD_REQUEST_PASSWORD.toString(), responseDTO.getStatus());
	}

	@Test
	void paramValidationInvalidPassword()
	{
		RequestDTO requestDTO = new RequestDTO("23423494-3", "1234567891234567891234", "example@email.com", "123");
		ResponseDTO responseDTO = new ResponseDTO();
		boolean isValid = ValidationUtil.validate(requestDTO, responseDTO,true);
		logger.info("isvalid "+isValid);
		logger.info("response "+ responseDTO);
		assertFalse(isValid);
		assertEquals(ErrorConstants.INVALID_PASS_FORMAT, responseDTO.getCode());
		assertEquals(ErrorConstantsEnum.NOT_ACCEPTABLE_PASS_FORMAT.toString(), responseDTO.getStatus());
	}
}
