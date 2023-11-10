//package org.oem.pinggo.filter;
//
//import jakarta.servlet.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.annotation.Order;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
//
//
//@Component
//@Order(1)
//@Slf4j
//public class TransactionFilter implements Filter {
//
//    @Override
//    public void doFilter(
//            ServletRequest request,
//            ServletResponse response,
//            FilterChain chain) throws IOException, ServletException {
//
//        HttpServletRequest req = (HttpServletRequest) request;
//
//        log.info(
//                "Starting a transaction for req : {}",
//                req.getRequestURI());
//
//        chain.doFilter(request, response);
//        log.info(
//                "Committing a transaction for req : {}",
//                req.getRequestURI());
//    }
//
//    // other methods
//}
