package nl.markpost.aiassistant.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import nl.markpost.aiassistant.api.model.Error;
import nl.markpost.aiassistant.exception.ExceptionHandler;
import nl.markpost.aiassistant.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtAuthenticationFilterTest {

  @Mock private ExceptionHandler exceptionHandler;

  @Mock private ObjectMapper objectMapper;

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private FilterChain filterChain;

  @Mock private PrintWriter writer;

  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @BeforeEach
  void setUp() throws Exception {
    jwtAuthenticationFilter = new JwtAuthenticationFilter(exceptionHandler, objectMapper);
    ReflectionTestUtils.setField(
        jwtAuthenticationFilter,
        "excludedPaths",
        new String[] {"/actuator/**", "/swagger-ui/**", "/api-docs/**"});
    ReflectionTestUtils.setField(
        jwtAuthenticationFilter, "publicKeyUrl", "https://auth.example.com/public-key");
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("Should identify preflight OPTIONS requests")
  void isPreflightRequest_optionsMethod() {
    when(request.getMethod()).thenReturn("OPTIONS");
    assertTrue(jwtAuthenticationFilter.isPreflightRequest(request));
  }

  @Test
  @DisplayName("Should not identify GET requests as preflight")
  void isPreflightRequest_getMethod() {
    when(request.getMethod()).thenReturn("GET");
    assertFalse(jwtAuthenticationFilter.isPreflightRequest(request));
  }

  @Test
  @DisplayName("Should extract access token from cookies")
  void extractAccessToken_withAccessToken() {
    Cookie[] cookies = {
      new Cookie("other_cookie", "other_value"), new Cookie("access_token", "test_token_value")
    };
    when(request.getCookies()).thenReturn(cookies);

    String token = jwtAuthenticationFilter.extractAccessToken(request);
    assertEquals("test_token_value", token);
  }

  @Test
  @DisplayName("Should return null when no access token cookie")
  void extractAccessToken_noAccessToken() {
    Cookie[] cookies = {new Cookie("other_cookie", "other_value")};
    when(request.getCookies()).thenReturn(cookies);

    String token = jwtAuthenticationFilter.extractAccessToken(request);
    assertNull(token);
  }

  @Test
  @DisplayName("Should return null when cookies array is null")
  void extractAccessToken_noCookies() {
    when(request.getCookies()).thenReturn(null);

    String token = jwtAuthenticationFilter.extractAccessToken(request);
    assertNull(token);
  }

  @Test
  void isPreflightRequest_shouldReturnTrueForOptionsMethod() {
    when(request.getMethod()).thenReturn("OPTIONS");

    boolean result = jwtAuthenticationFilter.isPreflightRequest(request);

    assertThat(result).isTrue();
  }

  @Test
  void isPreflightRequest_shouldReturnFalseForGetMethod() {
    when(request.getMethod()).thenReturn("GET");

    boolean result = jwtAuthenticationFilter.isPreflightRequest(request);

    assertThat(result).isFalse();
  }

  @Test
  void isPreflightRequest_shouldReturnFalseForPostMethod() {
    when(request.getMethod()).thenReturn("POST");

    boolean result = jwtAuthenticationFilter.isPreflightRequest(request);

    assertThat(result).isFalse();
  }

  @Test
  void extractAccessToken_shouldExtractFromAuthorizationHeader() {
    String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...";
    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

    String result = jwtAuthenticationFilter.extractAccessToken(request);

    assertThat(result).isEqualTo(token);
  }

  @Test
  void extractAccessToken_shouldExtractFromCookie() {
    String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...";
    Cookie[] cookies =
        new Cookie[] {new Cookie("other_cookie", "value"), new Cookie("access_token", token)};
    when(request.getHeader("Authorization")).thenReturn(null);
    when(request.getCookies()).thenReturn(cookies);

    String result = jwtAuthenticationFilter.extractAccessToken(request);

    assertThat(result).isEqualTo(token);
  }

  @Test
  void extractAccessToken_shouldReturnNullWhenNoTokenPresent() {
    when(request.getHeader("Authorization")).thenReturn(null);
    when(request.getCookies()).thenReturn(null);

    String result = jwtAuthenticationFilter.extractAccessToken(request);

    assertThat(result).isNull();
  }

  @Test
  void extractAccessToken_shouldReturnNullWhenNoCookiesContainToken() {
    Cookie[] cookies = new Cookie[] {new Cookie("other_cookie", "value")};
    when(request.getHeader("Authorization")).thenReturn(null);
    when(request.getCookies()).thenReturn(cookies);

    String result = jwtAuthenticationFilter.extractAccessToken(request);

    assertThat(result).isNull();
  }

  @Test
  void extractAccessToken_shouldPrioritizeAuthorizationHeader() {
    String headerToken = "header-token";
    when(request.getHeader("Authorization")).thenReturn("Bearer " + headerToken);

    String result = jwtAuthenticationFilter.extractAccessToken(request);

    assertThat(result).isEqualTo(headerToken);
  }

  @Test
  void extractAccessToken_shouldHandleInvalidAuthorizationHeader() {
    when(request.getHeader("Authorization")).thenReturn("Invalid token");
    when(request.getCookies()).thenReturn(null);

    String result = jwtAuthenticationFilter.extractAccessToken(request);

    assertThat(result).isNull();
  }

  @Test
  void extractAccessToken_shouldHandleAuthorizationHeaderWithoutBearer() {
    when(request.getHeader("Authorization")).thenReturn("Token abc123");
    when(request.getCookies()).thenReturn(null);

    String result = jwtAuthenticationFilter.extractAccessToken(request);

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("doFilterInternal should allow OPTIONS preflight requests without authentication")
  void doFilterInternal_shouldAllowPreflightRequest() throws Exception {
    when(request.getMethod()).thenReturn("OPTIONS");
    when(request.getRequestURI()).thenReturn("/api/v1/sessions");
    when(request.getContextPath()).thenReturn("");

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verify(response, never()).setStatus(anyInt());
  }

  @Test
  @DisplayName("doFilterInternal should allow excluded paths without authentication")
  void doFilterInternal_shouldAllowExcludedPaths() throws Exception {
    when(request.getRequestURI()).thenReturn("/actuator/**");
    when(request.getContextPath()).thenReturn("");
    when(request.getMethod()).thenReturn("GET");

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verify(response, never()).setStatus(anyInt());
  }

  @Test
  @DisplayName("doFilterInternal should return 401 when no access token provided")
  void doFilterInternal_shouldReturn401WhenNoToken() throws Exception {
    when(request.getRequestURI()).thenReturn("/api/v1/sessions");
    when(request.getContextPath()).thenReturn("");
    when(request.getMethod()).thenReturn("GET");
    when(request.getHeader("Authorization")).thenReturn(null);
    when(request.getCookies()).thenReturn(null);
    when(response.getWriter()).thenReturn(writer);

    Error error = new Error();
    error.setStatus(401);
    error.setMessage("Unauthorized");
    ResponseEntity<Error> errorResponse = new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    when(exceptionHandler.handleGenericExceptionException(any(UnauthorizedException.class)))
        .thenReturn(errorResponse);
    when(objectMapper.writeValueAsString(any())).thenReturn("{\"status\":401}");

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(response, atLeastOnce()).setStatus(401);
    verify(response).setContentType("application/json");
    verify(filterChain, never()).doFilter(request, response);
  }

  @Test
  @DisplayName("doFilterInternal should handle UnauthorizedException and return error response")
  void doFilterInternal_shouldHandleUnauthorizedException() throws Exception {
    // Given
    when(request.getRequestURI()).thenReturn("/api/v1/sessions");
    when(request.getContextPath()).thenReturn("");
    when(request.getMethod()).thenReturn("GET");
    when(request.getHeader("Authorization")).thenReturn(null);
    when(request.getCookies()).thenReturn(null);
    when(request.getHeader("Origin")).thenReturn("https://example.com");
    when(response.getWriter()).thenReturn(writer);

    Error error = new Error();
    error.setStatus(401);
    error.setCode("UNAUTHORIZED");
    error.setMessage("No access token provided");
    ResponseEntity<Error> errorResponse = new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    when(exceptionHandler.handleGenericExceptionException(any(UnauthorizedException.class)))
        .thenReturn(errorResponse);
    when(objectMapper.writeValueAsString(any())).thenReturn("{\"status\":401}");

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(response, atLeastOnce()).setStatus(401);
    verify(response).setContentType("application/json");
    verify(response).setHeader("Access-Control-Allow-Origin", "https://example.com");
    verify(response).setHeader("Access-Control-Allow-Credentials", "true");
    verify(writer).write("{\"status\":401}");
    verify(filterChain, never()).doFilter(request, response);
  }

  @Test
  @DisplayName("doFilterInternal should handle generic exceptions and return 401")
  void doFilterInternal_shouldHandleGenericException() throws Exception {
    when(request.getRequestURI()).thenReturn("/api/v1/sessions");
    when(request.getContextPath()).thenReturn("");
    when(request.getMethod()).thenReturn("GET");
    when(request.getHeader("Authorization")).thenReturn("Bearer invalid-token");
    when(response.getWriter()).thenReturn(writer);

    // Simulate getOrFetchPublicKey throwing an exception
    JwtAuthenticationFilter spyFilter = spy(jwtAuthenticationFilter);
    doThrow(new RuntimeException("Key fetch failed")).when(spyFilter).getOrFetchPublicKey();

    Error error = new Error();
    error.setStatus(401);
    error.setMessage("Unauthorized");
    ResponseEntity<Error> errorResponse = new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    when(exceptionHandler.handleGenericExceptionException(any(UnauthorizedException.class)))
        .thenReturn(errorResponse);
    when(objectMapper.writeValueAsString(any())).thenReturn("{\"status\":401}");

    spyFilter.doFilterInternal(request, response, filterChain);

    verify(response, atLeastOnce()).setStatus(401);
    verify(response).setContentType("application/json");
    verify(writer).write("{\"status\":401}");
    verify(filterChain, never()).doFilter(request, response);
  }

  @Test
  @DisplayName("doFilterInternal should add CORS headers on error")
  void doFilterInternal_shouldAddCorsHeadersOnError() throws Exception {
    when(request.getRequestURI()).thenReturn("/api/v1/sessions");
    when(request.getContextPath()).thenReturn("");
    when(request.getMethod()).thenReturn("GET");
    when(request.getHeader("Authorization")).thenReturn(null);
    when(request.getCookies()).thenReturn(null);
    when(request.getHeader("Origin")).thenReturn("https://frontend.example.com");
    when(response.getWriter()).thenReturn(writer);

    Error error = new Error();
    ResponseEntity<Error> errorResponse = new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    when(exceptionHandler.handleGenericExceptionException(any())).thenReturn(errorResponse);
    when(objectMapper.writeValueAsString(any())).thenReturn("{}");

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(response).setHeader("Access-Control-Allow-Origin", "https://frontend.example.com");
    verify(response).setHeader("Access-Control-Allow-Credentials", "true");
  }

  @Test
  @DisplayName("doFilterInternal should handle IOException when writing error response")
  void doFilterInternal_shouldHandleIOExceptionWhenWriting() throws Exception {
    when(request.getRequestURI()).thenReturn("/api/v1/sessions");
    when(request.getContextPath()).thenReturn("");
    when(request.getMethod()).thenReturn("GET");
    when(request.getHeader("Authorization")).thenReturn(null);
    when(request.getCookies()).thenReturn(null);
    when(response.getWriter()).thenReturn(writer);

    Error error = new Error();
    ResponseEntity<Error> errorResponse = new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    when(exceptionHandler.handleGenericExceptionException(any())).thenReturn(errorResponse);
    when(objectMapper.writeValueAsString(any())).thenReturn("{}");
    doThrow(new java.io.IOException("Write failed")).when(writer).write(anyString());

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(response, atLeastOnce()).setStatus(401);
    verify(filterChain, never()).doFilter(request, response);
  }

  @Test
  @DisplayName("doFilterInternal should strip context path from request URI")
  void doFilterInternal_shouldStripContextPath() throws Exception {
    when(request.getRequestURI()).thenReturn("/myapp/api/v1/sessions");
    when(request.getContextPath()).thenReturn("/myapp");
    when(request.getMethod()).thenReturn("GET");
    when(request.getHeader("Authorization")).thenReturn(null);
    when(request.getCookies()).thenReturn(null);
    when(response.getWriter()).thenReturn(writer);

    Error error = new Error();
    ResponseEntity<Error> errorResponse = new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    when(exceptionHandler.handleGenericExceptionException(any())).thenReturn(errorResponse);
    when(objectMapper.writeValueAsString(any())).thenReturn("{}");

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(response, atLeastOnce()).setStatus(401);
  }

  @Test
  @DisplayName("doFilterInternal should handle null context path")
  void doFilterInternal_shouldHandleNullContextPath() throws Exception {
    when(request.getRequestURI()).thenReturn("/api/v1/sessions");
    when(request.getContextPath()).thenReturn(null);
    when(request.getMethod()).thenReturn("GET");
    when(request.getHeader("Authorization")).thenReturn(null);
    when(request.getCookies()).thenReturn(null);
    when(response.getWriter()).thenReturn(writer);

    Error error = new Error();
    ResponseEntity<Error> errorResponse = new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    when(exceptionHandler.handleGenericExceptionException(any())).thenReturn(errorResponse);
    when(objectMapper.writeValueAsString(any())).thenReturn("{}");

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(response, atLeastOnce()).setStatus(401);
  }

  @Test
  @DisplayName("doFilterInternal should not add CORS headers when no Origin header")
  void doFilterInternal_shouldNotAddCorsHeadersWithoutOrigin() throws Exception {
    when(request.getRequestURI()).thenReturn("/api/v1/sessions");
    when(request.getContextPath()).thenReturn("");
    when(request.getMethod()).thenReturn("GET");
    when(request.getHeader("Authorization")).thenReturn(null);
    when(request.getCookies()).thenReturn(null);
    when(request.getHeader("Origin")).thenReturn(null);
    when(response.getWriter()).thenReturn(writer);

    Error error = new Error();
    ResponseEntity<Error> errorResponse = new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    when(exceptionHandler.handleGenericExceptionException(any())).thenReturn(errorResponse);
    when(objectMapper.writeValueAsString(any())).thenReturn("{}");

    jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

    verify(response, atLeastOnce()).setStatus(401);
    verify(response, never()).setHeader(eq("Access-Control-Allow-Origin"), anyString());
  }
}
