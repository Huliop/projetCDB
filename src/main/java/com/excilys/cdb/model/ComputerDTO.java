package com.excilys.cdb.model;

public class ComputerDTO {
	private Integer id;
	private String name;
	private String introduced;
	private String discontinued;
	private String companyName;
	private Integer companyId;

	private ComputerDTO() {
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

	public String getIntroduced() {
		return introduced;
	}

	public void setIntroduced(String introduced) {
		this.introduced = introduced;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public static class ComputerDTOBuilder {
		private Integer id;
		private String name;
		private String introduced;
		private String discontinued;
		private String companyName;
		private Integer companyId;

		public ComputerDTOBuilder withId(Integer id) {
			this.id = id;
			return this;
		}

		public ComputerDTOBuilder withName(String name) {
			this.name = name;
			return this;
		}

		public ComputerDTOBuilder withIntroduced(String introduced) {
			this.introduced = introduced;
			return this;
		}

		public ComputerDTOBuilder withDiscontinued(String discontinued) {
			this.discontinued = discontinued;
			return this;
		}

		public ComputerDTOBuilder withCompanyName(String companyName) {
			this.companyName = companyName;
			return this;
		}

		public ComputerDTOBuilder withCompanyId(Integer companyId) {
			this.companyId = companyId;
			return this;
		}

		public ComputerDTO build() {
			ComputerDTO computer = new ComputerDTO();
			computer.id = this.id;
			computer.name = this.name;
			computer.introduced = this.introduced;
			computer.discontinued = this.discontinued;
			computer.companyName = this.companyName;
			computer.companyId = this.companyId;
			return computer;
		}
	}

	@Override
	public String toString() {
		return "ComputerDTO [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued="
				+ discontinued + ", companyName=" + companyName + ", companyId=" + companyId + "]";
	}
}
