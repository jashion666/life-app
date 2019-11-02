package com.app.application.test.model;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class PersonDto {
  private String homeCode;
  private String name;
  private String lastTime;
  private String thisTime;
}
