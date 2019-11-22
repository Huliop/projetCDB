package console;

import java.lang.reflect.Field;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import core.exceptions.ComputerNotFoundException;
import core.exceptions.InvalidDataException;
import core.exceptions.UnsupportedActionException;
import core.model.Computer;
import core.model.ComputerDTO;
import core.model.Page;
import service.CompanyService;
import service.ComputerService;

public class CDB {

	private final static Client client = ClientBuilder.newClient()
			.register(HttpAuthenticationFeature.digest("leroigreedou", "lebojack"));

	static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) throws InvalidDataException {

		int choice = 0;

		while (choice != Actions.EXIT.getCode()) {
			printMenu();
			try {
				choice = scan.nextInt();
				try {
					switch (Actions.fromCode(choice)) {
					case ADD_COMPUTER:
						createComputer();
						break;
					case DELETE_COMPUTER:
						deleteComputer();
						break;
					case UPDATE_COMPUTER:
						updateComputer();
						break;
					case EXIT:
						System.exit(0);
						break;
					case LIST_ALL_COMPANIES:
						listAllCompanies();
						break;
					case LIST_ALL_COMPUTERS:
						listAllComputers();
						break;
					case GET_COMPUTER_BY_ID:
						getComputerById();
						break;
					case LIST_ALL_COMPUTERS_PAGE:
						listAllComputersPaginated();
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

	private static void printMenu() {
		System.out.println("Bienvenue sur CDB !");
		System.out.println("Veuillez faire votre choix dans ce menu :");

		Actions.asList().stream().forEach(action -> {
			System.out.println(action.getCode() + " - " + action.getMessage());
		});
	}

	private static void createComputer() throws InvalidDataException {
		/*
		 * ComputerDTO newComputer = new ComputerDTO.ComputerDTOBuilder().build(); for
		 * (Field field : ComputerDTO.class.getDeclaredFields()) {
		 * System.out.println("Veuillez saisir le " + field.getName() + " du computer");
		 * switch (field.getName()) { case "id": break; case "name":
		 * newComputer.setName(scan.next()); break; case "introduced":
		 * newComputer.setIntroduced(scan.nextLine().trim() != null ? scan.next() :
		 * null); break; case "discontinued":
		 * newComputer.setDiscontinued(scan.nextLine().trim() != null ? scan.next() :
		 * null); break; case "companyId": newComputer.setCompanyId(scan.nextInt());
		 * break; default: System.out.println("Attribut inconnu : " + field.getName());
		 * break; } } computerService.create(newComputer);
		 * System.out.println("Ordinateur créé ! " + newComputer.getId());
		 */
	}

	private static void deleteComputer() {
		/*
		 * System.out.print("Saisir l'id de l'ordinateur à supprimer : ");
		 * computerService.delete(scan.nextInt());
		 */
	}

	private static void updateComputer() throws InvalidDataException {
		/*
		 * System.out.print("Saisir l'id de l'ordinateur à modifier : "); ComputerDTO
		 * computerToUpdate = new ComputerDTO.ComputerDTOBuilder().build(); try {
		 * computerToUpdate = computerService.get(scan.nextInt()); } catch
		 * (ComputerNotFoundException e) {
		 * System.out.println("Cet ordinateur n'existe pas"); } boolean
		 * otherFieldToUpdate = true;
		 * 
		 * while (otherFieldToUpdate) {
		 * System.out.println("Quel champs souhaitez vous modifier ?");
		 * UpdateActions.asList().stream().forEach(action -> {
		 * System.out.println(action.getCode() + " - " + action.getMessage()); });
		 * 
		 * int fieldToUpdate = scan.nextInt(); updateField(computerToUpdate,
		 * fieldToUpdate);
		 * 
		 * System.out.println("Souhaitez-vous modifier un autre champs ? (y/n)");
		 * otherFieldToUpdate = scan.next().equals("y") ? true : false; }
		 * computerService.update(computerToUpdate);
		 */
	}

	private static void updateField(ComputerDTO computerToUpdate, int fieldToUpdate) {
		switch (fieldToUpdate) {
		case 1:
			showOldValueAndAskForNew(computerToUpdate.getName());
			computerToUpdate.setName(scan.next());
			break;
		case 2:
			showOldValueAndAskForNew(computerToUpdate.getIntroduced().toString());
			computerToUpdate.setIntroduced(scan.next());
			break;
		case 3:
			showOldValueAndAskForNew(computerToUpdate.getDiscontinued().toString());
			computerToUpdate.setDiscontinued(scan.next());
			break;
		case 4:
			showOldValueAndAskForNew(computerToUpdate.getCompanyId().toString());
			computerToUpdate.setCompanyId(scan.nextInt());
			break;
		default:
			System.out.println("Champs inconnu");
		}
	}

	private static void showOldValueAndAskForNew(String field) {
		System.out.println("Ancienne valeur : " + field);
		System.out.print("Saisir la nouvelle valeur : ");
	}

	private static void listAllComputers() {

		WebTarget webTarget = client.target("http://localhost:8080/webapp/computers");

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		List<ComputerDTO> result = invocationBuilder.get().readEntity(new GenericType<List<ComputerDTO>>() {});

		result.stream().forEach(System.out::println);
	}

	private static void getComputerById() {

		System.out.print("Merci de saisir l'id que vous souhaitez rechercher : ");
		int id = scan.nextInt();

		WebTarget webTarget = client.target("http://localhost:8080/webapp/computers").path(String.valueOf(id));

		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		ComputerDTO result = invocationBuilder.get().readEntity(ComputerDTO.class);

		System.out.println(result);
	}

	private static void listAllComputersPaginated() throws UnsupportedActionException {
		/*
		 * Page<Computer> page = new Page<Computer>(); boolean nextAction = true;
		 * 
		 * while (nextAction) { computerService.get(page, "");
		 * page.getElements().stream().forEach(System.out::println); System.out.
		 * println("Que souhaitez-vous faire ? (p : précédent, s : suivant, q : quitter"
		 * ); String answer = scan.next(); switch (answer) { case "p":
		 * page.setCurrentPage(page.getCurrentPage() - 1); break; case "s":
		 * page.setCurrentPage(page.getCurrentPage() + 1); break; case "q": nextAction =
		 * false; break; default: throw new UnsupportedActionException(); } }
		 */
	}

	private static void listAllCompanies() {
		/*
		 * companyService.get().stream().forEach(System.out::println);
		 */
	}
}
