package nl.markpost.aiassistant.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;

@ExtendWith(MockitoExtension.class)
class TraceparentFilterTest {

  @Mock private HttpServletRequest request;

  @Mock private HttpServletResponse response;

  @Mock private FilterChain filterChain;

  private TraceparentFilter traceparentFilter;

  @BeforeEach
  void setUp() {
    traceparentFilter = new TraceparentFilter();
    MDC.clear();
  }

  @AfterEach
  void tearDown() {
    MDC.clear();
  }

  @Test
  void doFilter_shouldExtractTraceparentFromHeader() throws IOException, ServletException {
    String traceparent = "00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01";
    when(request.getHeader("traceparent")).thenReturn(traceparent);

    traceparentFilter.doFilter(request, response, filterChain);

    assertThat(MDC.get("traceparent")).isEqualTo(traceparent);
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void doFilter_shouldGenerateTraceparentWhenNotPresent() throws IOException, ServletException {
    when(request.getHeader("traceparent")).thenReturn(null);

    traceparentFilter.doFilter(request, response, filterChain);

    String traceparent = MDC.get("traceparent");
    assertThat(traceparent).isNotNull();
    assertThat(traceparent).matches("^00-[0-9a-f-]{32,36}-[0-9a-f-]{16,36}-01$");
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void doFilter_shouldGenerateUniqueTraceparents() throws IOException, ServletException {
    when(request.getHeader("traceparent")).thenReturn(null);

    traceparentFilter.doFilter(request, response, filterChain);
    String traceparent1 = MDC.get("traceparent");
    MDC.clear();

    traceparentFilter.doFilter(request, response, filterChain);
    String traceparent2 = MDC.get("traceparent");

    assertThat(traceparent1).isNotEqualTo(traceparent2);
  }

  @Test
  void doFilter_shouldHandleNonHttpRequest() throws IOException, ServletException {
    traceparentFilter.doFilter(null, null, filterChain);

    String traceparent = MDC.get("traceparent");
    assertThat(traceparent).isNotNull();
    assertThat(traceparent).matches("^00-[0-9a-f-]{32,36}-[0-9a-f-]{16,36}-01$");
    verify(filterChain).doFilter(null, null);
  }
}
