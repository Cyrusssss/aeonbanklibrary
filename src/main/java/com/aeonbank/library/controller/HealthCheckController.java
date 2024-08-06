package com.aeonbank.library.controller;

import com.aeonbank.library.common.Enums;
import com.aeonbank.library.dto.BaseRequestResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestController
public class HealthCheckController {

    @GetMapping("/api/v1/healthcheck")
    public ResponseEntity<BaseRequestResponse<Map<String, Object>>> getHealthCheck (RequestEntity<byte[]> requestEntity, HttpServletRequest request) {
        BaseRequestResponse<Map<String, Object>> baseResponse = new BaseRequestResponse<>();
        try {
            Map<String, Object> clientIPs = new LinkedHashMap<>();
            clientIPs.put("HttpServletRequest.remoteAddr", request.getRemoteAddr());
            clientIPs.put("HttpServletRequest.localAddr", request.getLocalAddr());
            Enumeration<String> headers = request.getHeaderNames();
            while (headers.hasMoreElements()) {
                String key = headers.nextElement();
                String value = request.getHeader(key);
                clientIPs.put("HttpServletRequest.HEADERS.".concat(key), value);
            }
            clientIPs.put("header.host.address", (requestEntity.getHeaders().getHost().getAddress() == null ? "null" : requestEntity.getHeaders().getHost().getAddress().toString()));
            requestEntity.getHeaders().forEach((key, value) -> clientIPs.put("header.entrySet.".concat(key), value.toString()));
            clientIPs.put("body", (requestEntity.getBody() == null ? "null" : new String(requestEntity.getBody(), StandardCharsets.UTF_8)));

            //log.info("getHealthCheck.clientIPs >>> {}", clientIPs);
            baseResponse.setData(clientIPs);
            baseResponse.setStatus(Enums.Status.SUCCESS);
        } catch (Exception ex) {
            log.error("{getHealthCheck.ex} >>> ", ex);
        }
        return ResponseEntity.ok(baseResponse);
    }

}
