package com.session.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionListResponse {

	private List<SessionResponseDto> content;

	private long totalElements;

}
