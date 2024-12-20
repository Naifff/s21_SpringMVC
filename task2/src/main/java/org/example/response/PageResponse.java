package org.example.response;

import java.util.List;

public class PageResponse<T> {
	private List<T> content;
	private int pageNumber;
	private int pageSize;
	private long totalElements;
	private int totalPages;
	private boolean isFirst;
	private boolean isLast;

	public PageResponse(List<T> content, int pageNumber, int pageSize,
						long totalElements, int totalPages,
						boolean isFirst, boolean isLast) {
		this.content = content;
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
		this.totalPages = totalPages;
		this.isFirst = isFirst;
		this.isLast = isLast;
	}

	// Геттеры
	public List<T> getContent() {
		return content;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public boolean getIsFirst() {
		return isFirst;
	}

	public boolean getIsLast() {
		return isLast;
	}
}