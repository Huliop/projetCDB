package com.excilys.cdb.model;

public class Company {
	private Integer id;
	private String name;
	
	private Company() {
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public static class CompanyBuilder {
		private Integer id;
		private String name;
		
		public CompanyBuilder withId(Integer id) {
			this.id = id;
			return this;
		}
		
		public CompanyBuilder withName(String name) {
			this.name = name;
			return this;
		}
		
		public Company build() {
			Company company = new Company();
			company.id = this.id;
			company.name = this.name;
			return company;
		}
		
	}

	@Override
	public String toString() {
		return "Company [id = " + id + ", name = " + name + "]";
	}

}
