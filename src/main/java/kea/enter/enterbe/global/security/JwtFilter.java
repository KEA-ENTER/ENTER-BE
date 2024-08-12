package kea.enter.enterbe.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import kea.enter.enterbe.api.auth.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter { // OncePerRequestFilter -> 한 번 실행 보장

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        if(authorizationHeader == null) authorizationHeader = wrappedRequest.getHeader("Authorization");

        //JWT가 헤더에 있는 경우
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                Long userId = jwtUtil.getMemberId(token);

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId.toString());

                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }

        filterChain.doFilter(request, response); // 다음 필터로 넘기기
    }
}

