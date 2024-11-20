//package myblog.global.configure;
//
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class DuplicateRequestFilter implements Filter {
//
//    private final ConcurrentHashMap<String, Long> requestCache = new ConcurrentHashMap<>();
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest servletRequest1 = (HttpServletRequest) servletRequest;
//
//        // 요청 식별자 생성
//        String requestKey  = servletRequest1.getRequestURI()
//                + servletRequest1.getRemoteAddr()
//                + servletRequest1.getMethod();
//        System.out.println(requestKey);
//
//        // 요청이 이미 처리된 경우 중단
//        if (requestCache.containsKey(requestKey)) {
//            Long lastRequestTime = requestCache.get(requestKey);
//
//            // 요청 간격 체크 (예: 1000ms)
//            if (System.currentTimeMillis() - lastRequestTime < 100) {
//                return; // 요청 무시
//            }
//        }
//        // 새로운 요청 시간 저장
//        requestCache.put(requestKey, System.currentTimeMillis());
//
//        try {
//            filterChain.doFilter(servletRequest, servletResponse); // 요청 처리
//        } finally {
//            // 요청 캐시 제거 (선택 사항: 메모리 관리)
//            requestCache.remove(requestKey);
//        }
//
//    }
//}
