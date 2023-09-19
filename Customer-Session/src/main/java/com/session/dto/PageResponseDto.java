package com.session.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class PageResponseDto {

	List<SessionResponseDto> sessionResponseDtoList;
	Long totalElements;

}
