package com.example.anniee.utilities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class EntityResponse <T>{

    private int statusCode;
    private String message;
    private T entity;
}
