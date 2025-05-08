package com.krenai.reviewandrating.security;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
//
//public class FirebaseAuthFilter implements Filter
//{

//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//        throws IOException, ServletException{
//        HttpServletRequest req = (HttpServletRequest) request;
//        String authHeader = req.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        String idToken = authHeader.substring(7);
//
//        try {
//            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
//            // You can add the decoded info into the request attribute if needed
//            req.setAttribute("firebaseUser", decodedToken);
//            chain.doFilter(request, response);
//        } catch (Exception e) {
//            ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        }
//    }
//  }

    public class FirebaseAuthFilter extends OncePerRequestFilter{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            try {
                FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                String uid = decodedToken.getUid();

                // Set authenticated user
               UsernamePasswordAuthenticationToken authentication =
                       new UsernamePasswordAuthenticationToken(uid, null, List.of()); // authorities if needed

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (FirebaseAuthException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request,response);
    }
}
