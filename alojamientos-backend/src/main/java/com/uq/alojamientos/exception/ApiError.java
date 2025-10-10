package com.uq.alojamientos.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;

@Data @AllArgsConstructor
public class ApiError {
  private String message;
  private String path;
  private Instant timestamp;
}
