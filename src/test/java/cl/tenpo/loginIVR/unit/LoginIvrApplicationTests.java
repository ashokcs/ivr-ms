package cl.tenpo.loginIVR.unit;

import cl.tenpo.loginIVR.TestBase;
import cl.tenpo.loginIVR.api.dto.RequestDTO;
import cl.tenpo.loginIVR.component.LoginMSClient;
import cl.tenpo.loginIVR.exception.TenpoException;
import cl.tenpo.loginIVR.model.ErrorConstants;
import cl.tenpo.loginIVR.service.PasswordValidationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.http.HttpStatus.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginIvrApplicationTests extends TestBase {

	static Logger logger = LoggerFactory.getLogger(LoginIvrApplicationTests.class);

	@Autowired
	private PasswordValidationService passwordValidationService;

	@MockBean
	private LoginMSClient loginMSClient;

	@Test
	public void processLogin_BadRequest_RequestNull() {
		try{
			passwordValidationService.validatePassword(null);
		}catch (TenpoException e){
			Assert.assertEquals("El error debe ser", ErrorConstants.MISSING_PARAMETERS, e.getErrorCode());
			Assert.assertEquals("El error debe ser", BAD_REQUEST, e.getCode());
			throw e;
		}
	}

	@Test
	public void processLogin_BadRequest_MailNull() {
		try{
			passwordValidationService.validatePassword(RequestDTO.builder().build());
		}catch (TenpoException e){
			Assert.assertEquals("El error debe ser", ErrorConstants.MISSING_PARAMETERS, e.getErrorCode());
			Assert.assertEquals("El error debe ser", BAD_REQUEST, e.getCode());
			throw e;
		}
	}

	@Test
	public void processLogin_BadRequest_ClaveNull() {
		try{
			passwordValidationService.validatePassword(RequestDTO.builder()
					.email("test@test.com")
					.build());
		}catch (TenpoException e){
			Assert.assertEquals("El error debe ser", ErrorConstants.MISSING_PARAMETERS, e.getErrorCode());
			Assert.assertEquals("El error debe ser", BAD_REQUEST, e.getCode());
			throw e;
		}
	}

	@Test
	public void processLogin_BadRequest_AppNull() {
		try{
			passwordValidationService.validatePassword(RequestDTO.builder()
					.email("test@test.com")
					.password("1234")
					.build());
		}catch (TenpoException e){
			Assert.assertEquals("El error debe ser", ErrorConstants.MISSING_PARAMETERS, e.getErrorCode());
			Assert.assertEquals("El error debe ser", BAD_REQUEST, e.getCode());
			throw e;
		}
	}

	/*@Test
	public void processLogin_Unprocessable_PasswordLoked() {
			given(userLoginService.findByEmail(Mockito.anyString()))
					.willReturn(Optional.of(UserDTO.builder()
							.status(UserStatusEnum.PASSWORD_LOCKED.getStatus())
							.build()));

			ResponseEntity responseEntity = passwordValidationService.validatePassword(RequestDTO.builder()
					.rut("1231232-3")
					.call_id("1234567890123456789012")
					.password("1234")
					.build());
			ResponseDTO responseDTO = ((ResponseDTO) responseEntity.getBody());
			Assert.assertEquals("El error debe ser", ErrorConstants.PASSWORD_LOCKED, responseDTO.getErrorCode());
			Assert.assertEquals("El error debe ser", UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
			//verify(userLoginService, times(1)).findByEmail(Mockito.anyString());
	}

	@Test
	public void processLogin_Unprocessable_PasswordLokedAttempt3() {
		try{
			given(userLoginService.findByEmail(Mockito.anyString()))
					.willReturn(Optional.of(UserDTO.builder()
							.id(UUID.randomUUID())
							.status(UserStatusEnum.ACTIVE.getStatus())
							.attempt(0)
							.build()));
			given(azureClient.loginUser(Mockito.anyString(),Mockito.anyString()))
					.willThrow(new TenpoException(HttpStatus.UNPROCESSABLE_ENTITY, 9002));

			given(userRestClient.getUser(Mockito.anyString()))
					.willReturn(Optional.of(UserResponse.builder()
							.id(UUID.randomUUID())
							.build()));

			given(userLoginService.addAttempt(Mockito.any(UUID.class)))
					.willReturn(UserDTO.builder()
							.id(UUID.randomUUID())
							.status(UserStatusEnum.ACTIVE.getStatus())
							.attempt(1)
							.build());

			given(userLoginService.updateStatus(Mockito.any(UUID.class),Mockito.any(UserStatusEnum.class)))
					.willReturn(UserDTO.builder().build());

			passwordValidationService.validatePassword(RequestDTO.builder()
					.email("test@test.com")
					.password("1234")
					.build());
		}catch (TenpoException e){
			Assert.assertEquals("El error debe ser", ErrorConstants.INVALID_CREDENTIALS, e.getErrorCode());
			Assert.assertEquals("El error debe ser", NOT_FOUND, e.getCode());
			verify(userLoginService, times(0)).save(Mockito.anyString());
			verify(userLoginService,times(1)).addAttempt(Mockito.any(UUID.class));
			throw e;
		}
	}

	//No es un usuario no se guarda registro de login
	@Test
	public void processLogin_AzureLoginFailNoTenpoUserCredentialInvalid() {
		try{
			given(userLoginService.findByEmail(Mockito.anyString()))
					.willReturn(Optional.of(UserDTO.builder()
							.status(UserStatusEnum.ACTIVE.getStatus())
							.attempt(0)
							.build()));
			given(azureClient.loginUser(Mockito.anyString(),Mockito.anyString()))
					.willThrow(new TenpoException(HttpStatus.UNPROCESSABLE_ENTITY, 9002));

			given(userRestClient.getUser(Mockito.anyString()))
					.willReturn(Optional.empty());

			passwordValidationService.validatePassword(RequestDTO.builder()
					.email("test@test.com")
					.password("1234")
					.build());
		}catch (TenpoException e){
			Assert.assertEquals("El error debe ser", ErrorConstants.INVALID_CREDENTIALS, e.getErrorCode());
			Assert.assertEquals("El error debe ser", NOT_FOUND, e.getCode());
			verify(userLoginService, times(0)).save(Mockito.anyString());
			throw e;
		}
	}

	@Test
	public void processLogin_AzureLoginFailExistUserWrongPassword() {
		try{
			given(userLoginService.findByEmail(Mockito.anyString()))
					.willReturn(Optional.of(UserDTO.builder()
							.id(UUID.randomUUID())
							.status(UserStatusEnum.ACTIVE.getStatus())
							.attempt(0)
							.build()));
			given(azureClient.loginUser(Mockito.anyString(),Mockito.anyString()))
					.willThrow(new TenpoException(HttpStatus.UNPROCESSABLE_ENTITY, 9002));

			given(userRestClient.getUser(Mockito.anyString()))
					.willReturn(Optional.of(UserResponse.builder()
							.id(UUID.randomUUID())
							.build()));

			given(userLoginService.addAttempt(Mockito.any(UUID.class)))
					.willReturn(UserDTO.builder()
							.id(UUID.randomUUID())
							.status(UserStatusEnum.ACTIVE.getStatus())
							.attempt(1)
							.build());

			passwordValidationService.validatePassword(RequestDTO.builder()
					.email("test@test.com")
					.password("1234")
					.build());
		}catch (TenpoException e){
			Assert.assertEquals("El error debe ser", ErrorConstants.INVALID_CREDENTIALS, e.getErrorCode());
			Assert.assertEquals("El error debe ser", NOT_FOUND, e.getCode());
			verify(userLoginService, times(0)).save(Mockito.anyString());
			verify(userLoginService,times(1)).addAttempt(Mockito.any(UUID.class));
			throw e;
		}
	}

	@Test
	public void processLogin_AzureLoginFailNotExistUser() {
		try{
			given(userLoginService.findByEmail(Mockito.anyString()))
					.willReturn(Optional.empty());

			given(azureClient.loginUser(Mockito.anyString(),Mockito.anyString()))
					.willThrow(new TenpoException(HttpStatus.UNPROCESSABLE_ENTITY, 9002));

			given(userRestClient.getUser(Mockito.anyString()))
					.willReturn(Optional.of(UserResponse.builder()
							.id(UUID.randomUUID())
							.build()));

			given(userLoginService.addAttempt(Mockito.any(UUID.class)))
					.willReturn(UserDTO.builder()
							.id(UUID.randomUUID())
							.email("test@test.com")
							.status(UserStatusEnum.ACTIVE.getStatus())
							.attempt(1)
							.build());

			given(userLoginService.save(eq("test@test.com")))
					.willReturn(UserDTO.builder()
							.id(UUID.randomUUID())
							.email("test@test.com")
							.status(UserStatusEnum.ACTIVE.getStatus())
							.attempt(0)
							.build());

			passwordValidationService.validatePassword(RequestDTO.builder()
					.email("test@test.com")
					.password("1234")
					.build());
		}catch (TenpoException e){
			Assert.assertEquals("El error debe ser", ErrorConstants.INVALID_CREDENTIALS, e.getErrorCode());
			Assert.assertEquals("El error debe ser", NOT_FOUND, e.getCode());
			verify(userLoginService, times(1)).save(Mockito.anyString());
			verify(userLoginService,times(1)).addAttempt(Mockito.any(UUID.class));
			throw e;
		}
	}

	@Test
	public void processLogin_AzureLoginFailAttemp3() {
		try{
			given(userLoginService.findByEmail(Mockito.anyString()))
					.willReturn(Optional.empty());

			given(azureClient.loginUser(Mockito.anyString(),Mockito.anyString()))
					.willThrow(new TenpoException(HttpStatus.UNPROCESSABLE_ENTITY, 9002));

			given(userRestClient.getUser(Mockito.anyString()))
					.willReturn(Optional.of(UserResponse.builder()
							.id(UUID.randomUUID())
							.build()));

			given(userLoginService.addAttempt(Mockito.any(UUID.class)))
					.willReturn(UserDTO.builder()
							.id(UUID.randomUUID())
							.email("test@test.com")
							.status(UserStatusEnum.ACTIVE.getStatus())
							.attempt(3)
							.build());

			given(userLoginService.save(eq("test@test.com")))
					.willReturn(UserDTO.builder()
							.id(UUID.randomUUID())
							.email("test@test.com")
							.status(UserStatusEnum.ACTIVE.getStatus())
							.attempt(0)
							.build());
			doNothing().when(userRestClient).lockUserAzure(Mockito.any(UUID.class));

			passwordValidationService.validatePassword(RequestDTO.builder()
					.email("test@test.com")
					.password("1234")
					.build());
		}catch (TenpoException e){
			Assert.assertEquals("El error debe ser", ErrorConstants.PASSWORD_LOCKED, e.getErrorCode());
			Assert.assertEquals("El error debe ser", UNPROCESSABLE_ENTITY, e.getCode());
			verify(userLoginService, times(1)).save(Mockito.anyString());
			verify(userLoginService,times(1)).addAttempt(Mockito.any(UUID.class));
			verify(userRestClient,times(1)).lockUserAzure(Mockito.any(UUID.class));
			throw e;
		}
	}

	@Test
	public void processLogin_ok_ressetAttempt() {
		try{
			given(userLoginService.findByEmail(Mockito.anyString()))
					.willReturn(Optional.of(UserDTO.builder()
							.id(UUID.randomUUID())
							.status(UserStatusEnum.ACTIVE.getStatus())
							.attempt(0)
							.build()));

			given(azureClient.loginUser(Mockito.anyString(),Mockito.anyString()))
					.willReturn(TokenResponse.builder()
							.accessToken("XXXXXTOKENXXXX")
							.tokenType("Bearer")
							.build());

			given(userRestClient.getUser(Mockito.anyString()))
					.willReturn(Optional.of(UserResponse.builder()
							.id(UUID.randomUUID())
							.build()));

			passwordValidationService.validatePassword(RequestDTO.builder()
					.email("test@test.com")
					.password("1234")
					.build());

			verify(userLoginService,times(1)).resetAttempt(Mockito.any(UUID.class));
		}catch (TenpoException e){
			fail("can't be here");
		}
	}

	/*@Test
	public void processLogin_ok_saveRegistry() {
		try {

			given(userLoginService.findByEmail(Mockito.anyString()))
					.willReturn(Optional.empty());

			given(azureClient.loginUser(Mockito.anyString(),Mockito.anyString()))
					.willReturn(TokenResponse.builder()
							.accessToken("XXXXXTOKENXXXX")
							.tokenType("Bearer")
							.build());

			given(userRestClient.getUser(Mockito.anyString()))
					.willReturn(Optional.of(UserResponse.builder()
							.id(UUID.randomUUID())
							.build()));

			passwordValidationService.validatePassword(RequestDTO.builder()
					.email("test@test.com")
					.password("1234")
					.build());

			verify(userLoginService, times(1)).save(Mockito.anyString());
		}catch (TenpoException e) {
			fail("can't be here");
		}
	}*/
}
