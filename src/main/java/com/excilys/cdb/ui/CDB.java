package com.excilys.cdb.ui;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.excilys.cdb.exceptions.ComputerNotFoundException;
import com.excilys.cdb.exceptions.InvalidDataException;
import com.excilys.cdb.exceptions.UnsupportedActionException;
import com.excilys.cdb.model.Company;
import com.excilys.cdb.model.Computer;
import com.excilys.cdb.model.Page;
import com.excilys.cdb.service.CompanyService;
import com.excilys.cdb.service.ComputerService;

import configuration.MainConfig;

@Component
public class CDB {
	@Autowired
	private ComputerService computerService;
	@Autowired
	private CompanyService companyService;

	private static ApplicationContext ctx;

	static Scanner scan =  new Scanner(System.in);

	public static void main(String[] args) throws InvalidDataException {

		int choice = 0;

		ctx = new AnnotationConfigApplicationContext(MainConfig.class);
		CDB main = ctx.getBean(CDB.class);

		while (choice != Actions.EXIT.getCode()) {
			main.printMenu();
			try {
				choice = scan.nextInt();
				try {
					switch (Actions.fromCode(choice)) {
					case ADD_COMPUTER:
						main.createComputer();
						break;
					case DELETE_COMPUTER:
						main.deleteComputer();
						break;
					case UPDATE_COMPUTER:
						main.updateComputer();
						break;
					case EXIT:
						System.exit(0);
						break;
					case LIST_ALL_COMPANIES:
						main.listAllCompanies();
						break;
					case LIST_ALL_COMPUTERS:
						main.listAllComputers();
						break;
					case GET_COMPUTER_BY_ID:
						main.getComputerById();
						break;
					case LIST_ALL_COMPUTERS_PAGE:
						main.listAllComputersPaginated();
						break;
					default:
						break;
					}
				} catch (UnsupportedActionException e) {
					System.out.println("Cette opération n'existe pas !");
				}
			} catch (InputMismatchException e) {
				System.out.println("Veuillez saisir un des chiffres ici présent !\n");
				scan.nextLine();
			}
		}
	}

	private void printMenu() {
		System.out.println("Bienvenue sur CDB !");
		System.out.println("Veuillez faire votre choix dans ce menu :");

		Actions.asList().stream().forEach(action -> {
			System.out.println(action.getCode() + " - " + action.getMessage());
		});
	}

	private void createComputer() throws InvalidDataException {
		Computer newComputer = new Computer.ComputerBuilder().build();
		for (Field field : Computer.class.getDeclaredFields()) {
			System.out.println("Veuillez saisir le " + field.getName() + " du computer");
			switch (field.getName()) {
				case "id":
					break;
				case "name":
					newComputer.setName(scan.next());
					break;
				case "introduced":
					newComputer.setIntroduced(scan.nextLine().trim() != null ? LocalDate.parse(scan.next()) : null);
					break;
				case "discontinued":
					newComputer.setDiscontinued(scan.next() != null ? LocalDate.parse(scan.next()) : null);
					break;
				case "company":
					newComputer.setCompany(new Company.CompanyBuilder().withId(scan.nextInt()).build());
					break;
				default:
					System.out.println("Attribut inconnu : " + field.getName());
					break;
			}
		}
		computerService.create(newComputer);
		System.out.println("Ordinateur créé ! " + newComputer.getId());
	}

	private void deleteComputer() {
		System.out.print("Saisir l'id de l'ordinateur à supprimer : ");
		computerService.delete(scan.nextInt());
	}

	private void updateComputer() throws InvalidDataException {
		System.out.print("Saisir l'id de l'ordinateur à modifier : ");
		Computer computerToUpdate = new Computer.ComputerBuilder().build();
		try {
			computerToUpdate = computerService.get(scan.nextInt());
		} catch (ComputerNotFoundException e) {
			System.out.println("Cet ordinateur n'existe pas");
		}
		boolean otherFieldToUpdate = true;

		while (otherFieldToUpdate) {
			System.out.println("Quel champs souhaitez vous modifier ?");
			UpdateActions.asList().stream().forEach(action -> {
				System.out.println(action.getCode() + " - " + action.getMessage());
			});

			int fieldToUpdate = scan.nextInt();
			updateField(computerToUpdate, fieldToUpdate);

			System.out.println("Souhaitez-vous modifier un autre champs ? (y/n)");
			otherFieldToUpdate = scan.next().equals("y") ? true : false;
		}
		computerService.update(computerToUpdate);
	}

	private void updateField(Computer computerToUpdate, int fieldToUpdate) {
		switch (fieldToUpdate) {
			case 1:
				showOldValueAndAskForNew(computerToUpdate.getName());
				computerToUpdate.setName(scan.next());
				break;
			case 2:
				showOldValueAndAskForNew(computerToUpdate.getIntroduced().toString());
				computerToUpdate.setIntroduced(LocalDate.parse(scan.next()));
				break;
			case 3:
				showOldValueAndAskForNew(computerToUpdate.getDiscontinued().toString());
				computerToUpdate.setDiscontinued(LocalDate.parse(scan.next()));
				break;
			case 4:
				showOldValueAndAskForNew(computerToUpdate.getCompany().getId().toString());
				computerToUpdate.setCompany(companyService.get(scan.nextInt()).get());
				break;
			default:
				System.out.println("Champs inconnu");
		}
	}

	private static void showOldValueAndAskForNew(String field) {
		System.out.println("Ancienne valeur : " + field);
		System.out.print("Saisir la nouvelle valeur : ");
	}

	private void listAllComputers() {
		computerService.get().stream().forEach(System.out::println);
	}

	private void getComputerById() {
		System.out.print("Merci de saisir l'id que vous souhaitez rechercher : ");
		int id = scan.nextInt();
		try {
			System.out.println(computerService.get(id));
		} catch (ComputerNotFoundException e) {
			System.out.println("Computer " + id + " not found !");
		}
	}

	private void listAllComputersPaginated()
			throws UnsupportedActionException {
		Page<Computer> page = new Page<Computer>();
		boolean nextAction = true;

		while (nextAction) {
			computerService.get(page, "", false);
			page.getElements().stream().forEach(System.out::println);
			System.out.println("Que souhaitez-vous faire ? (p : précédent, s : suivant, q : quitter");
			String answer = scan.next();
			switch (answer) {
				case "p":
					page.setCurrentPage(page.getCurrentPage() - 1);
					break;
				case "s":
					page.setCurrentPage(page.getCurrentPage() + 1);
					break;
				case "q":
					nextAction = false;
					break;
				default: throw new UnsupportedActionException();
			}
		}
	}

	private void listAllCompanies() {
		companyService.get().stream().forEach(System.out::println);
	}
}
