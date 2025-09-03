package com.reliaquest.api.model.dto;

public record ServerResponse<T>(T data, String status) {}
