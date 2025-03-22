package com.example.springdemo.common.result;

import java.util.List;

import lombok.Data;

@Data
public class TableResultResponse<T> {
  private List<T> rows;
  private long total;

  public TableResultResponse(List<T> rows, long total) {
    this.rows = rows;
    this.total = total;
  }

  public TableResultResponse() {
  }
}
