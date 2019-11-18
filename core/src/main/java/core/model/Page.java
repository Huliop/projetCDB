package core.model;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {
	private List<T> elements = new ArrayList<>();
	private int currentPage = 0;
	private int offset = 10;

	public List<T> getElements() {
		return elements;
	}

	public void setElements(List<T> elements) {
		this.elements = elements;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		if (currentPage < 0) {
			currentPage = 0;
		}
		this.currentPage = currentPage;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getFirstLimit() {
		return this.getCurrentPage() * this.getOffset();
	}
}
